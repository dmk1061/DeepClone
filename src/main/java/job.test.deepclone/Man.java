package job.test.deepclone;

import java.util.List;

class  Man <T>{
    private String name;
    private int age;

    public List<Man> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Man> numbers) {
        this.numbers = numbers;
    }

    private List<String> favoriteBooks;

    private List<Man> numbers;

    private int [] arr;
    T man;

    Man me;

    public int[] getArr() {
        return arr;
    }

    public T getMan() {
        return man;
    }

    public void setMan(T man) {
        this.man = man;
    }

    public  Man(){

    }

    public Man getMe() {
        return me;
    }

    public void setMe(Man me) {
        this.me = me;
    }

    public Man(String name, int age, List<String> favoriteBooks) {
        this.name = name;
        this.age = age;
        this.favoriteBooks = favoriteBooks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void setFavoriteBooks(List<String> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }

    public void setArr(int[] arr) {
        this.arr = arr;
    }
}