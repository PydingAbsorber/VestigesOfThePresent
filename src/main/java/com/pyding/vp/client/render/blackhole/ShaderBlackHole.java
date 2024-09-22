package com.pyding.vp.client.render.blackhole;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ShaderBlackHole {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public ShaderBlackHole() throws IOException {
        // Загрузка шейдеров
        vertexShaderID = loadShader("shaders/blackhole_vertex.glsl", GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader("shaders/blackhole_fragment.glsl", GL20.GL_FRAGMENT_SHADER);

        programID = GL20.glCreateProgram();

        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        GL20.glLinkProgram(programID);
    }

    public int loadShader(String path, int shaderType) throws IOException {
        ResourceLocation shaderResource = new ResourceLocation("vp", path);
        InputStream inputStream = Minecraft.getInstance().getResourceManager().open(shaderResource);

        String shaderCode = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        int shaderID = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);
        GL20.glUseProgram(programID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new RuntimeException("Error creating shader: " + GL20.glGetShaderInfoLog(shaderID, 1024));
        }
        return shaderID;
    }


    private String loadResource(String path) throws IOException {
        // Реализуйте метод для загрузки GLSL файлов
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void cleanup() {
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    public void setUniform(String name, float value) {
        int location = GL20.glGetUniformLocation(programID, name);
        GL20.glUniform1f(location, value);
    }

    public void setUniform(String name, float x, float y) {
        int location = GL20.glGetUniformLocation(programID, name);
        GL20.glUniform2f(location, x, y);
    }

}

