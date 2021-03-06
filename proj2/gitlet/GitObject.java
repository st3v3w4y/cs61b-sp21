package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

interface GitObject extends Serializable {

    static GitObject read(String id) {
        File file = join(Repository.OBJECTS_DIR, id.substring(0, 2), id.substring(2));
        return readObject(file, GitObject.class);
    }

    default String write() {
        String sha1 = hash();
        File dir = join(Repository.OBJECTS_DIR, sha1.substring(0, 2));
        File file = join(dir, sha1.substring(2));
        dir.mkdir();
        writeObject(file, this);
        return sha1;
    }

    default String hash() {
        return sha1(serialize(this));
    }
}
