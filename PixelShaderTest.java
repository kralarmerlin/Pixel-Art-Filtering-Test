package com.mygdx.game.test.shader;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PixelShaderTest extends Game implements InputProcessor {
    Viewport viewport;
    OrthographicCamera camera;
    SpriteBatch batch;

    Sprite sprite;
    float rotation = 0;

    ShaderProgram shader;
    ShaderProgram batchShader;

    @Override
    public void create() {
        viewport = new FitViewport(320, 180);
        camera = (OrthographicCamera) viewport.getCamera();
        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(this);

        shader = new ShaderProgram(Gdx.files.internal("vertex.glsl"), Gdx.files.internal("fragment.glsl"));
        if (!shader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());

        sprite = new Sprite(new Texture("King.png"));

        shader.bind();
        shader.setUniformf("u_texture_size", new Vector2(sprite.getRegionWidth(), sprite.getRegionHeight()));
    }

    Color clear = Color.TAN;

    @Override
    public void render() {

        sprite.setX(sprite.getX() + 15f * Gdx.graphics.getDeltaTime());

        if(sprite.getX() > 280) sprite.setX(20);
        sprite.setY(20);

        // RENDER

        Gdx.gl.glClearColor(clear.r, clear.g, clear.b, clear.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        batch.setShader(batchShader);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        sprite.setRotation(rotation);
        sprite.draw(batch);

        batch.end();
    }

    @Override
    public void resize (int width, int height) {
        viewport.update(width, height, true);
        // This calculation is only correct for FitViewport.
        shader.setUniformf("u_texels_per_pixel", viewport.getScreenHeight() / viewport.getWorldHeight());
    }

    @Override
    public void dispose () {

    }

    private void setNearestFilter() {
        sprite.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }
    private void setLinearFilter() {
        sprite.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Input.Keys.F:
                if(batchShader == null) {
                    batchShader = shader;
                    System.out.println("Shader Enabled");
                    setLinearFilter();
                }
                else {
                    batchShader = null;
                    System.out.println("Shader Disabled");
                    setNearestFilter();
                }
                break;
            case Input.Keys.Q:
                rotation+= 11.25f;
                break;
            case Input.Keys.E:
                rotation-= 11.25f;
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
