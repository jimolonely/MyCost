package com.jimo.mycost.func.fs;

/**
 * 记录金额和百分比
 *
 * @author jimo
 * @date 20-2-24 下午9:03
 */
public class MoneyPercent {

    private double money;

    private String percent;

    public MoneyPercent(double money, String percent) {
        this.money = money;
        this.percent = percent;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
