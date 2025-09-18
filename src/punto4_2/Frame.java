package punto4_2;

//Clase frame (Marco)
public class Frame {
    private Page page;
    private int frameAddress;
    private boolean available;
    private int[] physicalAddresses;

    public  Frame(Page page, int direction, int frameSize,int physicalAddress) {
        this.page = page;
        this.frameAddress = direction;
        this.available = true;
        this.physicalAddresses = new int[frameSize];
        for (int i = 0; i < frameSize; i++) {
            physicalAddresses[i] = physicalAddress + i + 1000;
        }
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getFrameAddress() {
        return frameAddress;
    }

    public void setFrameAddress(int frameAddress) {
        this.frameAddress = frameAddress;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int[] getPhysicalAddresses() {
        return physicalAddresses;
    }
}
