package job.test.deepclone;

import java.util.Arrays;
import java.util.List;

public class DeepClone {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        List<String> books = Arrays.asList("AA", "BB", "CC");
        Man man = new Man("Alex", 5, books);
        man.setMan(man);

        Man copy = CopyUtils.deepCopyInit(man);
        copy.setName("copyAlex");
        copy.setAge(8);
        copy.setMan(man);
        Man changedCopy = CopyUtils.deepCopyInit(copy);

    }

}
