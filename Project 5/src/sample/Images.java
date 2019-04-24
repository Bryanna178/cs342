package sample;

import javafx.scene.image.Image;

public class Images {
    private String Name;
    private Image Image;

    Images(String name, String imageName){
        this.Name = name;
        this.Image = new Image(imageName);
    }

    public String getName(){
        return this.Name;
    }
    public Image getImage(){
        return this.Image;
    }
}
