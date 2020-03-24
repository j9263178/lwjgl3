package Map;
import Render.Sheet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    static ArrayList<tile> tiles;
    String TileData,SolidData;
    int width=16,height=10;

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

    private void loadMap(String filename) throws IOException {
        String ori = new String(Files.readAllBytes(Paths.get(filename)));
        String baselayer=ori.split(",")[0].replace("\\","");
        String solidlayer=ori.split(",")[1].replace("\\","");

        byte[] a= Base64.getDecoder().decode(baselayer);
        byte[] b=decompress(a);

        byte[] c= Base64.getDecoder().decode(solidlayer);
        byte[] d=decompress(c);

        int tem=0;
        tiles=new ArrayList<>();
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
        Map fuck =new Map();
        fuck.loadMap("/Users/joseph/lwjgl3/src/test/java/Map/h.txt");
        for(tile t:tiles){
            System.out.println(t.id[0]+" "+t.id[1]+" "+ t.isSolid);
        }
    }

}
