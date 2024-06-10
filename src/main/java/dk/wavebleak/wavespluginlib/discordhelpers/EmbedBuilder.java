package dk.wavebleak.wavespluginlib.discordhelpers;



import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class EmbedBuilder {

    private String title = null;
    private String description = null;
    private String url = null;
    private Integer color = null;
    private String footer = null;
    private String image = null;
    private String thumbnail = null;
    private final List<Field> fields = new ArrayList<>();

    public Embed create() {
        return new Embed(title, description, url, color, footer, image, thumbnail, fields);
    }


    public EmbedBuilder addField(String name, String value, boolean inline) {
        fields.add(new Field(name, value, inline));
        return this;
    }

    public EmbedBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public EmbedBuilder setDescription(String description) {
        this.description = description;
        return this;
    }
    public EmbedBuilder setUrl(String url) {
        this.url = url;
        return this;
    }
    public EmbedBuilder setColor(Color color) {
        int rgb = color.getRed();
        rgb = (rgb << 8) + color.getGreen();
        rgb = (rgb << 8) + color.getBlue();
        this.color = rgb;
        return this;
    }
    public EmbedBuilder setFooter(String footer) {
        this.footer = footer;
        return this;
    }
    public EmbedBuilder setImage(String image) {
        this.image = image;
        return this;
    }
    public EmbedBuilder setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

}
