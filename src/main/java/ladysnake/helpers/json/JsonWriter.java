package ladysnake.helpers.json;

import com.google.gson.*;
import ladysnake.helpers.log.Logger;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**A class that allows to easily write JSON data to a JSON file
 *@author Ludwig GUERIN
 * @version 1.0.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JsonWriter {
    final static Gson JsonBuilder = new GsonBuilder().setPrettyPrinting().create();

    final static Charset charset = StandardCharsets.UTF_8;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A String holding the URI to the JSON file
     */
    protected String fileURI;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**Default constructor, seeking the file using the given URI
     * @param URI being the URI to the JSON file
     *  @throws JsonParseException if the file doesn't contain valid JSON
     *  @throws  IOException if the file cannot be found
     */
    public JsonWriter(String URI) throws JsonParseException, IOException {
        switchFile(URI);
    }

    /**Copy constructor
     * @param other being the {@link JsonWriter} to copy from
     *  @throws JsonParseException if the file doesn't contain valid JSON
     *  @throws  IOException if the file cannot be found
     */
    public JsonWriter(JsonWriter other) throws JsonParseException, IOException{
        this(other.fileURI);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**Write a {@link String} into the file
     * @param str being the {@link String} that will be written in the JSON file
     * @return this {@link JsonWriter} (for chaining purposes)
     * @throws IOException for any kind of errors whille attempting to read the file
     */
    public JsonWriter writeString(String str) throws IOException {
        Logger.triggerEvent(Logger.VERBOSE, "Writing `" + str + "` to " + this.fileURI);
        final Path path = Paths.get(this.fileURI);
        final BufferedWriter writer = Files.newBufferedWriter(path, charset, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        writer.append( JsonBuilder.toJson(str) );
        writer.close();
        return this;
    }

    /**Write a {@link JsonElement} into the file
     * @param elem being the {@link JsonElement} that will be written in the JSON file
     * @return this {@link JsonWriter} (for chaining purposes)
     * @throws IOException for any kind of errors whille attempting to read the file
     */
    public JsonWriter writeJsonElement(JsonElement elem) throws IOException{
        Logger.triggerEvent(Logger.VERBOSE, "Writing `" + elem.toString() + "` to " + this.fileURI);
        final Path path = Paths.get(this.fileURI);
        final BufferedWriter writer = Files.newBufferedWriter(path, charset, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        writer.append( JsonBuilder.toJson(elem) );
        writer.close();
        return this;
    }

    /**Switch the JSON file this {@link JsonWriter} is associated to
     * @param URI being the URI to the new JSON file
     * @return this {@link JsonWriter} (for chaining purposes)
     */
    public JsonWriter switchFile(String URI)/* throws IOException*/{
        Logger.triggerEvent(Logger.VERBOSE, "Switching writing file to " + this.fileURI);
        this.fileURI = URI;
        return this;
    }
}
