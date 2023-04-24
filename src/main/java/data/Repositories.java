package data;

public class Repositories {

    private static final SpyMasterRepo REPO = new MySqlRepo();

    private Repositories() {
    }

    public static SpyMasterRepo getSpyMasterRepo() {
        return REPO;
    }

}
