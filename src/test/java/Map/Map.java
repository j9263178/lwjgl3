package Map;
import Render.Sheet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class Map {

    private class tile{
        int[] id={0,0};
        boolean isSolid;
        tile(int x,int y,boolean is){
            id[0]=x;
            id[1]=y;
            isSolid=is;
        }
    }

    Sheet tileset;
    ArrayList<tile> tiles;
    String TileData,SolidData;
    int width,height;

    Map(){

    }

    public void render(){

    }

    public static byte[] decompress(byte[] data) {
        byte[] output = new byte[0];
        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        decompresser.end();
        return output;
    }

    private void loadMap(String filename){
        String baselayer="";
        String solidlayer="";
        byte[] a= Base64.getDecoder().decode(baselayer);
        byte[] b=decompress(a);
        byte[] c= Base64.getDecoder().decode(baselayer);
        byte[] d=decompress(c);
        int tem=0;

        for(int i=0;i<b.length;i++){
            if(i%4==0) {
                boolean is=false;
                if (b[i] < 0)
                    tem = 256 + b[i];
                else
                    tem = b[i];
                if(d[i]!=0) is=true;
                tiles.add(new tile(tem % width, tem / width + 1, is));
            }
        }
    }

    public static void main(String args[]) throws IOException, DataFormatException {
        String hi= "eJwNw0c7ggEAAODvr1CImz1uVuSWhnEzIm5Cxi0r3CLzFkJuZvHrvO/zvE1BEDQbMmyLrbYZsd0OO+2y2x577bPfAQcdctgRRx1z3KgTThpzyrjTJkyaMu2Ms84574KLLrlsxhVXzbrmuhvm3HTLbfPuuOue+xY88NAjjz2x6KlnnlvywkvLXnntjbfeeW/FBx99suqzL75a8813P/z0y29/rNvw1z//AaffKsc=";


        byte[] a= Base64.getDecoder().decode(hi);
        byte[] b=decompress(a);
        int cur=0;
        for(byte i:b) if(cur++%4==0) if(i<0) System.out.print(256+i+" "); else System.out.print(i+" ");

    }

}
