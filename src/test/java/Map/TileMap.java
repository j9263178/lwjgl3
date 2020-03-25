package Map;
import Render.Sheet;
import Render.Camera;
import Render.Shader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class TileMap {

    private Shader shader;
    private Camera camera;
    private Sheet tileSet;
    private ArrayList<Tile> tiles;
    private int MapWidth=0,MapHeight=0;
    float x=0;
    float y=0;
    private float tilesize;

    public TileMap(int w, int h, Sheet tileSet, float tilesize,Shader shader,Camera camera) {
        MapWidth=w;
        MapHeight=h;
        this.tileSet=tileSet;
        this.tilesize=tilesize;
        this.shader = shader;
        this.tileSet.setShader(this.shader);
        this.camera = camera;
    }

    public void render(){
        for(Tile t:tiles) t.draw();
    }

    public void loadMap(String filename) throws IOException {
        String ori = new String(Files.readAllBytes(Paths.get(filename)));
        String baselayer=ori.split(",")[0].replace("\\","");
        String solidlayer=ori.split(",")[1].replace("\\","");

        byte[] a= Base64.getDecoder().decode(baselayer);
        byte[] b=decompress(a);

        byte[] c= Base64.getDecoder().decode(solidlayer);
        byte[] d=decompress(c);

        int tem=0;
        tiles=new ArrayList<>();
        for(int i=0;i*4<b.length;i++){

           // int temx=((i+1) % MapWidth!=0)? (i) % MapWidth: MapWidth;
           // int temy=(i+1) / (MapWidth+1) + 1;
            int temx=((i) % MapWidth);
            int temy=((i) / MapWidth);
            float x=0+temx*tilesize;
            float y=0-temy*tilesize;

            boolean is=false;
            byte[] temb={b[i*4],b[i*4+1],b[i*4+2],b[i*4+3]};
            tem=convertirOctetEnEntier(temb);
            if(d[i]!=0) is=true;
            Tile tt=new Tile(x,y,tilesize,shader,camera,tileSet);
            tt.setSolid(is);
            tt.setId(tem);
            tiles.add(tt);
        }
    }

    public static void main(String args[]) throws IOException, DataFormatException {

    }

    public ArrayList<Tile> getTiles(){
        return this.tiles;
    }

    private int convertirOctetEnEntier(byte[] b){
        int MASK = 0xFF;
        int result = 0;
        result = b[0] & MASK;
        result = result + ((b[1] & MASK) << 8);
        result = result + ((b[2] & MASK) << 16);
        result = result + ((b[3] & MASK) << 24);
        return result;
    }
    private byte[] decompress(byte[] data) {
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
}
