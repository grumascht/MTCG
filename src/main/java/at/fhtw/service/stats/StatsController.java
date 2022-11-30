package at.fhtw.service.stats;

import at.fhtw.dal.repo.StatsRepo;
import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.models.Card;
import at.fhtw.models.User;
import at.fhtw.models.UserStats;
import at.fhtw.service.user.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static at.fhtw.service.Service.unitOfWork;

public class StatsController {
    public Response handleGetStats(Request request) {
        if(request.getAuthorizedClient() == null)
            return new Response(HttpStatus.UNAUTHORIZED,
                    ContentType.PLAIN_TEXT,
                    "");

        User user = new UserController().getUserWithUserName(request.getAuthorizedClient().getUsername());

        try {
            ResultSet resultSetStats = new StatsRepo().getStats(user, unitOfWork);

            if(resultSetStats.next()){
                UserStats userStats = new UserStats(resultSetStats.getInt(1),
                        resultSetStats.getInt(2),
                        resultSetStats.getInt(3),
                        user.getUsername());

                return new Response(HttpStatus.OK,
                        ContentType.JSON,
                        new ObjectMapper().writeValueAsString(userStats.getStats()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Response(HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.PLAIN_TEXT,
                "");
    }

    public Response handleGetScoreboard(Request request) {
        if(request.getAuthorizedClient() == null)
            return new Response(HttpStatus.UNAUTHORIZED,
                    ContentType.PLAIN_TEXT,
                    "");

        try {
            ResultSet resultSetStats = new StatsRepo().getAllStats(unitOfWork);

            List<Map<String, String>> stats = new ArrayList<Map<String, String>>();
            while(resultSetStats.next()){
                stats.add(new UserStats(resultSetStats.getInt(1),
                        resultSetStats.getInt(2),
                        resultSetStats.getInt(3),
                        resultSetStats.getString(4)).getStats());
            }

            return new Response(HttpStatus.OK,
                    ContentType.JSON,
                    new ObjectMapper().writeValueAsString(stats));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Response(HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.PLAIN_TEXT,
                "");
    }
}
