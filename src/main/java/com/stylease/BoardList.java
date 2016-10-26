package com.stylease;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class BoardList {

  private static int STATIC_BOARD_COUNT = 5;
  private static String[][] STATIC_MESSAGES = {
    {"It sifts from Leaden Sieves -",
    "It powders all the Wood. ",
    "It fills with Alabaster Wool ",
    "The Wrinkles of the Road -",
      },
    {"It makes an even Face ",
    "Of Mountain, and of Plain -",
    "Unbroken Forehead from the East ",
    "Unto the East again -",
        },
    {"It reaches to the Fence -",
    "It wraps it Rail by Rail ",
    "Till it is lost in Fleeces -",
    "It deals Celestial Vail ",
          },
    {"To Stump, and Stack - and Stem -",
    "A Summer’s empty Room - ",
    "Acres of Joints, where Harvests were, ",
    "Recordless, but for them -",
            },
    {"It Ruffles Wrists of Posts ",
    "As Ankles of a Queen -",
    "Then stills it’s Artisans - like Ghosts - ",
    "Denying they have been -",
              },
    {"THESE are the days when birds come back,",
    "A very few, a bird or two,",
    "To take a backward look.",
                },
    {"These are the days when skies put on",
    "The old, old sophistries of June,--",
    "A blue and gold mistake.",
                  },
    {"Oh, fraud that cannot cheat the bee,",
    "Almost thy plausibility",
    "Induces my belief,",
                    },
    {"Till ranks of seeds their witness bear,",
    "And softly through the altered air",
    "Hurries a timid leaf!",
                      },
    {"Oh, sacrament of summer days,",
    "Oh, last communion in the haze,",
    "Permit a child to join,",
    },
    {"Thy sacred emblems to partake,",
    "Thy consecrated bread to break,",
    "Taste thine immortal wine!",
    }
  };
  
  private ArrayList<Board> boards;
  
  private ArrayList<Board> getBoards() {
    ArrayList<Board> boardList = new ArrayList<>();
    for(int i = 0; i < STATIC_BOARD_COUNT; i++) {
      int messagesIdx = STATIC_MESSAGES.length % i;
      Board b = new Board(STATIC_MESSAGES[messagesIdx][i]);
      for(int j = 0; j < STATIC_MESSAGES[messagesIdx].length; j++) {
        Message m = new Message();
        m.setText(STATIC_MESSAGES[messagesIdx][j]);
        b.addMessage(m);
      }
    }
    
    return boardList;
  }
  
  public BoardList() {
    boards = getBoards();
  }
  
  @GetMapping("/b_list")
  public String showBoards(ModelMap model) {
    model.addAttribute("boards", boards);
    return "b_list";
  }
}
