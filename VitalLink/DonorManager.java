import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DonorManager {
    private static DonorManager instance;
    private List<Donor> donors;
    private final String fileName = "donors.dat";

    private DonorManager() {
        donors = new ArrayList<>();
        load();
    }

    public static DonorManager getInstance() {
        if(instance==null) instance = new DonorManager();
        return instance;
    }

    public void addDonor(Donor d){
        donors.add(d);
        save();
    }

    public void updateDonor(Donor dOld, Donor dNew){
        donors.remove(dOld);
        donors.add(dNew);
        save();
    }

    public void deleteDonor(Donor d){
        donors.remove(d);
        save();
    }

    public List<Donor> getAllDonors(){ return donors; }

    public List<Donor> search(String blood, String name){
        List<Donor> result = new ArrayList<>();
        for(Donor d: donors){
            boolean matchBlood = (blood==null || blood.isEmpty() || d.getBlood().equalsIgnoreCase(blood));
            boolean matchName = (name==null || name.isEmpty() || d.getName().toLowerCase().contains(name.toLowerCase()));
            if(matchBlood && matchName) result.add(d);
        }
        return result;
    }

    private void save(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))){
            oos.writeObject(donors);
        }catch(Exception e){ e.printStackTrace(); }
    }

    private void load(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))){
            donors = (List<Donor>) ois.readObject();
        }catch(FileNotFoundException e){
            donors = new ArrayList<>();
        }catch(Exception e){ e.printStackTrace(); }
    }
}