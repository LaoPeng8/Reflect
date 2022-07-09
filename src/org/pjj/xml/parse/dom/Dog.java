package org.pjj.xml.parse.dom;

/**
 * 这个Dog对象 对应于 XML中的一个<dog></dog>结点
 *     <dog id="1">
 *         <name>YAYA</name>
 *         <score>100</score>
 *         <level>10</level>
 *     </dog>
 */
public class Dog {
    private int id;
    private String name;
    private double score;
    private int level;

    public Dog() {
    }

    public Dog(int id, String name, double score, int level) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", level=" + level +
                '}';
    }
}
