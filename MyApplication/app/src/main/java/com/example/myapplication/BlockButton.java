package com.example.myapplication;


import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

public class BlockButton extends Button {

    int x; //버튼 x좌표
    int y; //버튼 y좌표
    boolean broken = false; //깨졌는지 아닌지
    boolean mine = false; //지뢰인지 아닌지
    int neighborMines; // 주변 Mine 개수
    boolean flag = false; //깃발인지 아닌지
    static int flags2 = 10;
    static int flags = 0; // 체크한 Flag 개수
    static int blocks = 81; // 남아있는 블록 개수 (10개가 되면 끝)

    public BlockButton(Context context, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
    }

    public void toggleFlag() { //깃발 꽂는 메소드
        if (this.flag) { //깃발이 꽂혀있으면 없애
            setFlag(false);
            this.broken = false;
            this.setText("");
            flags--;
            flags2++;
        } else { //깃발이 안 꽂혀있는데
            if (flags == 10) { //꽂은 깃발이 10개면 아무것도 못해
                this.setText("");
            } else { //깃발 꽂아
                this.setText("+");
                this.broken = true;
                setFlag(true);
                flags++;
                flags2--;
            }

        }


    }

    public boolean breakBlock() { //블록 깨는 메소드
        if(this.broken){ //깃발 때문에 broken이면 못 깨
            return false;
        } else if(isMine()){ //폭탄이라 게임 끝일때만 반환값을 true로
            this.setText("*");
            this.setEnabled(false);
            this.broken = true;
            return true;
        } else {
            this.setText(Integer.toString(neighborMines));
            this.setEnabled(false);
            this.broken = true;
            return false;
        }
    }

    public void end(){ //끝났을때 블록마다 공개하는 메소드
        this.broken = true;
        if(isMine()){ //폭탄이면 *
            this.setText("*");
        }
        else { //지뢰없으면 지뢰 개수
            this.setText(Integer.toString(neighborMines));
        }
        this.setEnabled(false);
    }

    public boolean isMine() {
        return mine;
    }
    public void setMine(boolean mine) {
        this.mine = mine;
    }
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getFlags(){
        return flags;
    }
    public int getFlags2(){
        return flags2;
    }


    public int getNeighborMines() {
        return neighborMines;
    }
    public void setNeighborMines(int neighborMines) {
        this.neighborMines = neighborMines;
    }
    public static int getBlocks() {
        return blocks;
    }
}