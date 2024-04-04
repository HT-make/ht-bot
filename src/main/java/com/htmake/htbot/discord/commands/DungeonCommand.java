package com.htmake.htbot.discord.commands;

import com.htmake.htbot.domain.dungeon.entity.Monster;
import com.htmake.htbot.unirest.HttpClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class DungeonCommand extends ListenerAdapter {

    private final HttpClient httpClient;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("던전-입장")) {
            handleDungeonInfo(event);
        }
    }

    private void handleDungeonInfo(SlashCommandInteractionEvent event) {

        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(event.getUser().getName())
                .setTitle(":key:던전")
                .setDescription(
                        """
                        :warning: 주의 :warning:
                        전투에서 패배 시 해당 던전에서 얻은
                        아이템을 모두 잃으니 주의하세요!
                        """)
                .build();

        StringSelectMenu menu = StringSelectMenu.create("dungeonMenu")
                .setPlaceholder("던전 선택")
                .addOptions(Arrays.asList(
                        SelectOption.of("드넓은 초원 | 권장 레벨 1~10", "enter-dungeon1-1"),
                        SelectOption.of("깊은 동굴 | 권장 레벨 10~20", "enter-dungeon2-1"),
                        SelectOption.of("끈적이는 늪 | 권장 레벨 20~30", "enter-dungeon3-1"),
                        SelectOption.of("어두운 숲 | 권장 레벨 30~40", "enter-dungeon4-1"),
                        SelectOption.of("몰락한 성 | 권장 레벨 40~50", "enter-dungeon5-1"),
                        SelectOption.of("용암 지대 | 권장 레벨 50~60", "enter-dungeon6-1")
                ))
                .build();

        event.replyEmbeds(embed)
                .addActionRow(menu)
                .addActionRow(Button.danger("cancel", "닫기"))
                .queue();
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        String component = event.getValues().get(0);
        String[] componentList = component.split("-");

        if (componentList[0].equals("enter")) {
            battleInterface(event, null, componentList);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String component = event.getComponentId();
        String[] componentList = component.split("-");

        if (componentList[0].equals("enter")) {
            battleInterface(null, event, componentList);
        }
    }

    private void battleInterface(StringSelectInteractionEvent selectEvent, ButtonInteractionEvent buttonEvent, String[] dungeonInfo) {
        String endPoint = "/dungeon/{dungeon_id}";
        Pair<String, String> routeParam = new Pair<>("dungeon_id", dungeonInfo[1]);

        HttpResponse<JsonNode> response = httpClient.sendGetRequest(endPoint, routeParam);

        if (response.getStatus() == 200) {
            handleSuccessfulResponse(selectEvent, buttonEvent, response, dungeonInfo[2]);
        } else {
            handleErrorResponse(selectEvent, buttonEvent, response);
        }
    }

    private void handleSuccessfulResponse(
            StringSelectInteractionEvent selectEvent,
            ButtonInteractionEvent buttonEvent,
            HttpResponse<JsonNode> response,
            String stage
    ) {
        JSONObject dungeonObject = response.getBody().getObject();
        JSONArray monsterArray = dungeonObject.getJSONArray("monsterList");

        ArrayList<Monster> monsterList = toMonsterList(monsterArray);
        monsterList.sort(Comparator.comparingInt(Monster::getLevel));
        String dungeonName = dungeonObject.getString("name") + "-" + stage;

        JSONObject playerObject = playerDataResponse(selectEvent, buttonEvent);
        Monster monster = randomMonster(monsterList, stage);

        MessageEmbed embed = buildEmbed(dungeonName, monster, playerObject);

        if (selectEvent != null) {
            selectEvent.editMessageEmbeds(embed)
                    .setActionRow(
                            Button.success("attack", "공격"),
                            Button.primary("potion-open", "포션"),
                            Button.danger("run", "후퇴")
                    )
                    .queue();
        } else {
            buttonEvent.getMessage().editMessageEmbeds(embed)
                    .setActionRow(
                            Button.success("attack", "공격"),
                            Button.primary("potion-open", "포션"),
                            Button.danger("run", "후퇴")
                    )
                    .queue();
        }
    }

    private void handleErrorResponse(
            StringSelectInteractionEvent selectEvent,
            ButtonInteractionEvent buttonEvent,
            HttpResponse<JsonNode> response
    ) {
        Message message = selectEvent != null ? selectEvent.getMessage() : buttonEvent.getMessage();

        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(":warning: 던전 입장")
                .setDescription("던전 입장에 실패하였습니다!")
                .build();

        message.editMessageComponents(Collections.emptyList()).queue();
        message.editMessageEmbeds(embed).queue();
        log.error(String.valueOf(response.getBody()));
    }

    private MessageEmbed buildEmbed(String dungeonName, Monster monster, JSONObject playerObject) {

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(dungeonName)
                .setDescription("Lv." + monster.getLevel() + " " + monster.getName())

                .addField(":crossed_swords: 공격력", "" + monster.getDamage(), true)
                .addField(":heart: 체력", "" + monster.getHealth(), true)
                .addField(":shield: 방어력", "" + monster.getDefence(), true)

                .addField(":video_game: 전투 현황", monster.getName() +"이/가 나타났다!", false)
                .addBlankField(false)

                .addField(":crossed_swords: 공격력", "" + playerObject.getInt("damage"), true)
                .addField(":heart: 체력", "" + playerObject.getInt("health"), true)
                .addField(":shield: 방어력", "" + playerObject.getInt("defence"), true)

                .addField(":large_blue_diamond: 마나", "" + playerObject.getInt("mana"), true)
                .addField(":boom: 치명타 확률", playerObject.getInt("criticalChance") + "%", true)
                .addField(":boom: 치명타 데미지", playerObject.getInt("criticalDamage") + "%", true)

                .setFooter("Lv." + playerObject.getInt("level") + " " + playerObject.get("name"))
                .build();
    }

    private ArrayList<Monster> toMonsterList(JSONArray monsterArray) {
        ArrayList<Monster> monsterList = new ArrayList<>();

        for (int i = 0; i < monsterArray.length(); i++) {
            JSONObject monsterObject = monsterArray.getJSONObject(i);

            Monster monster = Monster.builder()
                    .id(monsterObject.getString("id"))
                    .name(monsterObject.getString("name"))
                    .level(monsterObject.getInt("level"))
                    .damage(monsterObject.getInt("damage"))
                    .health(monsterObject.getInt("health"))
                    .defence(monsterObject.getInt("defence"))
                    .exp(monsterObject.getInt("exp"))
                    .gold(monsterObject.getInt("gold"))
                    .build();

            monsterList.add(monster);
        }

        return monsterList;
    }

    private Monster randomMonster(ArrayList<Monster> monsterList, String stage) {
        Random random = new Random();
        int stageInt = Integer.parseInt(stage);

        int min = (stageInt % 2 == 0 ? stageInt / 2 : stageInt / 2 + 1) - 1;
        int max = (stageInt % 2 == 0 ? min + 3 : min + 2) + 1;

        int ran = random.nextInt(min, max);

        return monsterList.get(ran);
    }

    private JSONObject playerDataResponse(StringSelectInteractionEvent selectEvent, ButtonInteractionEvent buttonEvent) {
        String playerId = selectEvent != null ? selectEvent.getUser().getId() : buttonEvent.getUser().getId();
        String endPoint = "/player/battle/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);

        HttpResponse<JsonNode> response = httpClient.sendGetRequest(endPoint, routeParam);

        if (response.getStatus() == 200) {
            return response.getBody().getObject();
        } else {
            handleErrorResponse(selectEvent, buttonEvent, response);
        }

        return null;
    }
}
