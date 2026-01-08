package pl.kiszkiel.realpay.obj;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private double balance;
    private String name;
    private List<OfflinePlayer> members = new ArrayList<>();
    private OfflinePlayer owner;

    public Bank(String name, OfflinePlayer owner, double balance, @Nullable List<OfflinePlayer> members) {
        this.name = name;
        this.owner = owner;
        this.balance = balance;
        if (members != null) this.members = members;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public List<OfflinePlayer> getMembers() {
        return members;
    }

    public void addMember(OfflinePlayer player) {
        this.members.add(player);
    }

    public void removeMember(OfflinePlayer player) {
        this.members.remove(player);
    }

    public OfflinePlayer getOwner() {
        return owner;
    }
}
