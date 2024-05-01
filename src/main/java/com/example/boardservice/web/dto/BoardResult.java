package com.example.boardservice.web.dto;


import com.example.boardservice.domain.board.Board;
import com.example.boardservice.domain.member.MemberOfBoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardResult {

  public static Map<String,Object> build(Board board, List<MemberOfBoard> members) {
    Map<String, Object> boardData = new HashMap<>();
    boardData.put("id", board.id());
    boardData.put("name", board.name());
    boardData.put("personal", board.isPersonal());

    List<MemberData> membersData = new ArrayList<>();
    for (MemberOfBoard user: members) {
      membersData.add(new MemberData(user));
    }

    Map<String,Object> result = new HashMap<>();
    result.put("board", boardData);
    result.put("members", membersData);

    return result;
  }

}
