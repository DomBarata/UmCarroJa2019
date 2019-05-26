import java.io.IOException;

public class wait {
    public wait(){
        try {
            System.in.read();
        }catch (IOException io){System.out.println(io);}
    }
}
