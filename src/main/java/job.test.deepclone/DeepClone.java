package job.test.deepclone;

import java.util.Arrays;
import java.util.List;

public class DeepClone {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        List<String> books = Arrays.asList("AA", "BB", "CC");
        ArchieMan man = new ArchieMan();
        man.setMan("Alex");
        man.setAge(5);
        man.setFavoriteBooks(books);
        man.setMan(man);
        man.setArr(new int[]{1, 3, 7});

        Man copy = CopyUtils.deepCopyInit(man);
        copy.setName("copyAlex");
        copy.setAge(8);
        copy.setMan("man");
        Man changedCopy = CopyUtils.deepCopyInit(copy);

    }

}
