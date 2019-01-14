/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolao.model;

/**
 *
 * @author thayn
 */
public class NumbersPerPlayer{
    int playerID, betID;
    String numbers;

    public NumbersPerPlayer(int playerID, int betID, String numbers){
        this.playerID = playerID;
        this.betID = betID;
        this.numbers = numbers;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getBetID() {
        return betID;
    }

    public String getNumbers() {
        return numbers;
    }

}