import java.io.Serializable;

public class Donor implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id, name, blood, dob, lastDonation, address, mobile, email;

    public Donor(String id, String name, String blood, String dob, String lastDonation,
                 String address, String mobile, String email){
        this.id = id;
        this.name = name;
        this.blood = blood;
        this.dob = dob;
        this.lastDonation = lastDonation;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
    }

    public String getId(){ return id; }
    public String getName(){ return name; }
    public String getBlood(){ return blood; }
    public String getDob(){ return dob; }
    public String getLastDonation(){ return lastDonation; }
    public String getAddress(){ return address; }
    public String getMobile(){ return mobile; }
    public String getEmail(){ return email; }

    public void setName(String n){ this.name = n; }
    public void setBlood(String b){ this.blood = b; }
    public void setDob(String d){ this.dob = d; }
    public void setLastDonation(String l){ this.lastDonation = l; }
    public void setAddress(String a){ this.address = a; }
    public void setMobile(String m){ this.mobile = m; }
    public void setEmail(String e){ this.email = e; }

    public interface DonorCommand {
        void execute();
    }

    public class UpdateNameCommand implements DonorCommand {
        private String newName;

        public UpdateNameCommand(String newName) {
            this.newName = newName;
        }

        public void execute() {
            setName(newName);
        }
    }

    public class UpdateBloodCommand implements DonorCommand {
        private String newBlood;

        public UpdateBloodCommand(String newBlood) {
            this.newBlood = newBlood;
        }

        public void execute() {
            setBlood(newBlood);
        }
    }

    public class UpdateAddressCommand implements DonorCommand {
        private String newAddress;

        public UpdateAddressCommand(String newAddress) {
            this.newAddress = newAddress;
        }

        public void execute() {
            setAddress(newAddress);
        }
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Donor other = (Donor) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }
}