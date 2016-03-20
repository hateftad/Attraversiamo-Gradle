package com.me.component;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.me.utils.Converters;


public class ShaderComponent extends BaseComponent {
	
	private Texture displacementTexture;
	private Matrix4 matrix;
	private ShaderProgram shader;
	private ShaderProgram waterShader;
	private Mesh waterMesh;
	private String vertexShader;
	private String fragmentShader;
	private String fragmentShader2;
	
	Matrix4 projection = new Matrix4();
	Matrix4 view = new Matrix4();
	Matrix4 model = new Matrix4();
	Matrix4 combined = new Matrix4();
    float time;

	public ShaderComponent(String extraTexture, Body body){
        displacementTexture = new Texture(Gdx.files.internal("data/level/common/waterdisplacement.png"));
        displacementTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        displacementTexture.bind();
        ShaderProgram.pedantic = false;
        vertexShader = Gdx.files.internal("data/shaders/vertex.vert").readString();
        fragmentShader = Gdx.files.internal("data/shaders/fragment.frag").readString();
        fragmentShader2 = Gdx.files.internal("data/shaders/fragment2.frag").readString();
        shader = new ShaderProgram(vertexShader, fragmentShader);
        waterShader = new ShaderProgram(vertexShader, fragmentShader2);
        matrix = new Matrix4();
        waterShader.setUniformMatrix("u_projTrans", matrix);
        PolygonShape shape = (PolygonShape) body.getFixtureList().get(0).getShape();
        ArrayList<Vector2> vertices = new ArrayList<>();
        for(int i = 0; i < shape.getVertexCount(); i++){
            Vector2 vertex = new Vector2();
            shape.getVertex(i, vertex);
            vertices.add(vertex);
		}
		waterMesh = createQuad(vertices.get(3).x * 100, vertices.get(3).y* 100,
								vertices.get(0).x* 100, vertices.get(0).y* 100,
								vertices.get(1).x* 100, vertices.get(1).y* 160,
								vertices.get(2).x* 100, vertices.get(2).y* 160);
	}

	public void render(SpriteBatch batch, OrthographicCamera camera, SpriteComponent sprite){
		float dt = Gdx.graphics.getDeltaTime();
		time += dt;
		float angle = time * (2 * MathUtils.PI);
		if (angle > (2 * MathUtils.PI))
			angle -= (2 * MathUtils.PI);
		
		projection.set(camera.projection);
		view.setToTranslation(-camera.position.x, -camera.position.y, -2.0f);
		model.setToTranslation(sprite.getPosition().x, sprite.getPosition().y, 0);
		combined.set(projection).mul(view).mul(model);
		
		batch.setShader(waterShader);
		batch.begin();
		waterShader.setUniformMatrix("u_mvpMatrix", combined);
		sprite.draw(batch);
		batch.end();
		
		//RENDER WATER
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		sprite.getTexture().bind(1);
		displacementTexture.bind(2);
		batch.setShader(null);
		shader.begin();
		shader.setUniformMatrix("u_mvpMatrix", combined);

		shader.setUniformi("u_texture", 1);
		shader.setUniformi("u_texture2", 2);
		shader.setUniformf("timedelta", angle * 2 );
		waterMesh.render(shader, GL20.GL_TRIANGLE_FAN);
		shader.end();
		
		displacementTexture.bind(0);
		displacementTexture.dispose();
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		//Gdx.gl20.glBlendFunc(GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_SRC_ALPHA);
		
		
	}
	float x;
	public Mesh createQuad(float x1, float y1, float x2, float y2, float x3,
			float y3, float x4, float y4) {
		float[] verts = new float[20];
		int i = 0;

		verts[i++] = x1; // x1
		verts[i++] = y1; // y1
		verts[i++] = 0;
		verts[i++] = 1f; // u1
		verts[i++] = 1f; // v1

		verts[i++] = x2; // x2
		verts[i++] = y2; // y2
		verts[i++] = 0;
		verts[i++] = 0f; // u2
		verts[i++] = 1f; // v2

		verts[i++] = x3; // x3
		verts[i++] = y3; // y2
		verts[i++] = 0;
		verts[i++] = 0f; // u3
		verts[i++] = 0f; // v3

		verts[i++] = x4; // x4
		verts[i++] = y4; // y4
		verts[i++] = 0;
		verts[i++] = 1f; // u4
		verts[i++] = 0f; // v4

		Mesh mesh = new Mesh(false, 4, 0, // static mesh with 4 vertices and no
											// indices
				new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE), 
				new VertexAttribute( Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

		mesh.setVertices(verts);
		return mesh;

	}
	
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		displacementTexture.dispose();
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub

	}

}
