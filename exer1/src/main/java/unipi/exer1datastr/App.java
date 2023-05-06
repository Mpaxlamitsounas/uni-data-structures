package unipi.exer1datastr;

public class App 
{
    public static void main(String[] args)
    {
        ArcList arc = new ArcList();
        for (int i = 0; i < 10; i++) {
            arc.insert(new Arc(i,i,i));
        }

    }
}
