package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.error;
import static gitlet.Utils.join;

class Gitlet {

    private final String[] args;

    public Gitlet(String[] args) {
        this.args = args;
    }

    public void run() throws IOException {
        if (args.length == 0) {
            throw error("Please enter a command.");
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init" -> init();
            case "add" -> add();
            case "commit" -> commit();
            case "rm" -> rm();
            case "log" -> log();
            case "global-log" -> globalLog();
            case "find" -> find();
            case "status" -> status();
            case "checkout" -> checkout();
            case "branch" -> branch();
            case "rm-branch" -> rmBranch();
            case "reset" -> reset();
            case "merge" -> merge();
            default -> throw error("No command with that name exists.");
        }
    }

    private void init() throws IOException {
        validateNumArgs(1);
        Repository.initialize();
    }

    private void add() {
        validateNumArgs(2);
        validateRepoInitialized();
        File file = join(Repository.CWD, args[1]);
        Repository.addFile(file);
    }

    private void commit() {
        validateNumArgs(2);
        validateRepoInitialized();
        String message = args[1];
        Repository.commit(message);
    }

    private void rm() {
        validateNumArgs(2);
        validateRepoInitialized();
        File file = join(Repository.CWD, args[1]);
        Repository.removeFile(file);
    }

    private void log() {
        validateNumArgs(1);
        validateRepoInitialized();
        System.out.println(Repository.getLog());
    }

    private void globalLog() {
        validateNumArgs(1);
        validateRepoInitialized();
        System.out.println(Repository.getGlobalLog());
    }

    private void find() {
        validateNumArgs(2);
        validateRepoInitialized();
        String message = args[1];
        String[] ids = Repository.getCommitIDByMsg(message);
        for (String id : ids) {
            System.out.println(id);
        }
    }

    private void status() {
        validateNumArgs(1);
        validateRepoInitialized();
        System.out.println(Repository.getStatus());
    }

    private void checkout() {
        validateRepoInitialized();
        File file;
        String commitID;
        String branchName;
        switch (args.length) {
            case 2 -> {
                branchName = args[1];
                Repository.checkoutToBranch(branchName);
            }
            case 3 -> {
                if (!args[1].equals("--")) {
                    throw error("Incorrect operands.");
                }
                file = join(Repository.CWD, args[2]);
                Repository.checkoutFile(file);
            }
            case 4 -> {
                if (!args[2].equals("--")) {
                    throw error("Incorrect operands.");
                }
                commitID = args[1];
                file = join(Repository.CWD, args[3]);
                Repository.checkoutFileToCommit(file, commitID);
            }
            default -> throw error("Incorrect operands.");
        }
    }

    private void branch() {
        validateNumArgs(2);
        validateRepoInitialized();
        String name = args[1];
        Repository.createBranch(name);
    }

    private void rmBranch() {
        validateNumArgs(2);
        validateRepoInitialized();
        String branchName = args[1];
        Repository.removeBranch(branchName);
    }

    private void reset() {
        validateNumArgs(2);
        validateRepoInitialized();
        String commitID = args[1];
        Repository.reset(commitID);
    }

    private void merge() {
        validateNumArgs(2);
        validateRepoInitialized();
        String branchName = args[1];
        Repository.mergeBranch(branchName);
    }

    private void validateNumArgs(int n) {
        if (args.length != n) {
            throw error("Incorrect operands.");
        }
    }

    private void validateRepoInitialized() {
        if (!Repository.isInitialized()) {
            throw error("Not in an initialized Gitlet directory.");
        }
    }
}