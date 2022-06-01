package ddwucom.mobile.hw02;

import java.io.Serializable;

public class AreaDto implements Serializable { // intent 넘어 갈 때, dto 객체 넘기려면 Serializealbe implements 해줘야한다.
                                                // ex) intent.putExtra(TAG, dto)
    private long id;
    private String name;
    private String phone;
    private String address;

    public long getId() { return id; }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return id + ". " + address + " - " + name + " (" + phone + ")" + "\n";
    }

}
