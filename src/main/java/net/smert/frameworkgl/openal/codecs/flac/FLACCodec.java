package net.smert.frameworkgl.openal.codecs.flac;

import net.smert.frameworkgl.openal.codecs.Codec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class FLACCodec implements Codec {

    private final static Logger log = LoggerFactory.getLogger(FLACCodec.class);

    @Override
    public Data load(String audioFile) {
        log.info("Loading FLAC audio file: {}", audioFile);
        return new Codec.Data();
    }

}
