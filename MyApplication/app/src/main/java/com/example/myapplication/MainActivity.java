package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView textResult;
    Integer result;
    String tmp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = 10;

        textResult = (TextView)findViewById(R.id.textView);
        tmp = "Mine : " + result.toString();
        textResult.setText(String.valueOf(tmp));

        //테이블 만들고 9개 행 삽입
        TableLayout table = findViewById(R.id.tableLayout);
        TableRow[] tableRow = new TableRow[9];
        for (int i = 0; i < 9; i++) {
            tableRow[i] = new TableRow(this);
            table.addView(tableRow[i]);
        }

        //레이아웃 생성
        TableRow.LayoutParams layoutParams =
                new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f);

        //버튼 81개 만들고 행에 삽입
        BlockButton[][] buttons = new BlockButton[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                buttons[i][j] = new BlockButton(this, i, j);
                buttons[i][j].setLayoutParams(layoutParams);
                tableRow[i].addView(buttons[i][j]);
            }
        }

        //랜덤으로 폭탄 10개 생성
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            int a = (int) (Math.random() * 8);
            int b = (int) (Math.random() * 8);
            if (buttons[a][b].isMine()) {
                i--;
            } else {
                buttons[a][b].setMine(true);
            }
        }

        // 주변 폭탄 개수 세기
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                int count=0;
                if (buttons[i][j].mine) continue;
                if((i >= 1 && i <= 8) && (j >= 1 && j <= 8)) { // 왼쪽 위 대각선
                    if(buttons[i-1][j-1].mine) count++;
                }
                if((i >= 1 && i <= 8) && (j >= 0 && j <= 8)) { // 위
                    if(buttons[i-1][j].mine) count++;
                }
                if((i >= 1 && i <= 8) && (j >= 0 && j <= 7)) { // 오른쪽 위 대각선
                    if(buttons[i-1][j+1].mine) count++;
                }

                if((i >= 0 && i <= 8) && (j >= 0 && j <= 7)) {  //오른쪽
                    if(buttons[i][j+1].mine) count++;
                }
                if((i >= 0 && i <= 7) && (j >= 0 && j <= 7)) {  // 오른쪽 아래 대각선
                    if(buttons[i+1][j+1].mine) count++;
                }
                if((i >= 0 && i <= 7) && (j >= 0 && j <= 8)) {  // 아래
                    if(buttons[i+1][j].mine) count++;
                }
                if((i >= 0 && i <= 7) && (j >= 1 && j <= 8)) {  // 왼쪽 아래 대각선
                    if(buttons[i+1][j-1].mine) count++;
                }
                if((i >= 0 && i <= 8) && (j >= 1 && j <= 8)) { // 왼쪽
                    if(buttons[i][j-1].mine) count++;
                }
                buttons[i][j].setNeighborMines(count);
            }
        }

        ToggleButton togglebutton = findViewById(R.id.toggleButton);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(togglebutton.isChecked()) { // ON(flag) 상태이면
                            ((BlockButton) view).toggleFlag();

                            result=((BlockButton) view).getFlags2();
                            textResult = (TextView)findViewById(R.id.textView);
                            tmp = "Mine : " + result.toString();
                            textResult.setText(String.valueOf(tmp));

                            if(BlockButton.getBlocks() == 10) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "성공", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        } else { //OFF(break) 상태이면
                            if (((BlockButton) view).breakBlock()) { //지뢰가 터지면 true가 오고 끝
                                for (int i = 0; i < 9; i++) {
                                    for (int j = 0; j < 9; j++) {
                                        buttons[i][j].end();
                                    }
                                }
                                Toast toast = Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_LONG);
                                toast.show();
                            } else{
                                if(!((BlockButton) view).isFlag()){
                                    openBlock(((BlockButton) view)); //재귀로 돌려서 블록을 깨는데
                                    if (BlockButton.getBlocks() == 10) { //남은 블록이 10개면 성공
                                        Toast toast = Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }
                            }
                        }
                    }

                    private void openBlock(BlockButton cell) {
                        cell.breakBlock();
                        if (cell.getNeighborMines() != 0) { // 0이 아니면 하나만 깨
                            BlockButton.blocks--;
                            System.out.println(BlockButton.getBlocks());
                            return;
                        } else { //0이면 상하좌우로 재귀
                            BlockButton.blocks--;
                            // 꼭짓점 4개
                            if (cell.x== 0 && cell.y == 0) { //[0][0]
                                if(!buttons[cell.x + 1][cell.y].broken)
                                    openBlock(buttons[cell.x + 1][cell.y]);
                                if(!buttons[cell.x][cell.y + 1].broken)
                                    openBlock(buttons[cell.x][cell.y + 1]);
                            }
                            else if (cell.x == 0 && cell.y == 8) { //[0][8]
                                if(!buttons[cell.x + 1][cell.y].broken)
                                    openBlock(buttons[cell.x + 1][cell.y]);
                                if(!buttons[cell.x][cell.y - 1].broken)
                                    openBlock(buttons[cell.x][cell.y - 1]);
                            }
                            else if (cell.x == 8 && cell.y == 0) { //[8][0]
                                if(!buttons[cell.x - 1][cell.y].broken)
                                    openBlock(buttons[cell.x - 1][cell.y]);
                                if(!buttons[cell.x][cell.y + 1].broken)
                                    openBlock(buttons[cell.x][cell.y + 1]);
                            }
                            else if (cell.x == 8 && cell.y == 8) { //[8][8]
                                if(!buttons[cell.x - 1][cell.y].broken)
                                    openBlock(buttons[cell.x - 1][cell.y]);
                                if(!buttons[cell.x][cell.y - 1].broken)
                                    openBlock(buttons[cell.x][cell.y - 1]);
                            }
                            // 모서리 네 변
                            else if (cell.x == 0 && (0 < cell.y && cell.y < 8)) { //[0][1~7]
                                if(!buttons[cell.x + 1][cell.y].broken)
                                    openBlock(buttons[cell.x + 1][cell.y]);
                                if(!buttons[cell.x][cell.y + 1].broken)
                                    openBlock(buttons[cell.x][cell.y + 1]);
                                if(!buttons[cell.x][cell.y - 1].broken)
                                    openBlock(buttons[cell.x][cell.y - 1]);
                            }
                            else if (cell.x == 8 && (0 < cell.y && cell.y < 8)) { //[8][1~7]
                                if(!buttons[cell.x - 1][cell.y].broken)
                                    openBlock(buttons[cell.x - 1][cell.y]);
                                if(!buttons[cell.x][cell.y + 1].broken)
                                    openBlock(buttons[cell.x][cell.y + 1]);
                                if(!buttons[cell.x][cell.y - 1].broken)
                                    openBlock(buttons[cell.x][cell.y - 1]);
                            }
                            else if ((0 < cell.x && cell.x <8) && cell.y == 0) { //[1~7][0]
                                if(!buttons[cell.x][cell.y + 1].broken)
                                    openBlock(buttons[cell.x][cell.y + 1]);
                                if(!buttons[cell.x + 1][cell.y].broken)
                                    openBlock(buttons[cell.x + 1][cell.y]);
                                if(!buttons[cell.x - 1][cell.y].broken)
                                    openBlock(buttons[cell.x - 1][cell.y]);
                            }
                            else if ((0 < cell.x && cell.x <8) && cell.y == 8) { //[1~7][8]
                                if(!buttons[cell.x][cell.y - 1].broken)
                                    openBlock(buttons[cell.x][cell.y - 1]);
                                if(!buttons[cell.x + 1][cell.y].broken)
                                    openBlock(buttons[cell.x + 1][cell.y]);
                                if(!buttons[cell.x - 1][cell.y].broken)
                                    openBlock(buttons[cell.x - 1][cell.y]);
                            }
                            // 나머지
                            else{
                                if(!buttons[cell.x - 1][cell.y].broken)
                                    openBlock(buttons[cell.x - 1][cell.y]);
                                if(!buttons[cell.x + 1][cell.y].broken)
                                    openBlock(buttons[cell.x + 1][cell.y]);
                                if(!buttons[cell.x][cell.y - 1].broken)
                                    openBlock(buttons[cell.x][cell.y - 1]);
                                if(!buttons[cell.x][cell.y + 1].broken)
                                    openBlock(buttons[cell.x][cell.y + 1]);
                            }
                        }
                    }
                });
            }
        }
    }
}