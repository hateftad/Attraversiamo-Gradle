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
	
	private Texture m_displacementTexture;
	private Matrix4 m_matrix;
	private ShaderProgram m_shader;
	private ShaderProgram m_waterShader;
	private Mesh m_waterMesh;
	private String vertexShader;
	private String fragmentShader;
	private String fragmentShader2;
	
	Matrix4 projection = new Matrix4();
	Matrix4 view = new Matrix4();
	Matrix4 model = new Matrix4();
	Matrix4 combined = new Matrix4();
	
	public ShaderComponent(String extraTexture, Body body){
		m_displacementTexture = new Texture(Gdx.files.internal("data/level/common/waterdisplacement.png"));
		m_displacementTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		m_displacementTexture.bind();
		ShaderProgram.pedantic = false;
		vertexShader = Gdx.files.internal("data/shaders/vertex.vert").readString();
		fragmentShader = Gdx.files.internal("data/shaders/fragment.frag").readString();
		fragmentShader2 = Gdx.files.internal("data/shaders/fragment2.frag").readString();
		m_shader = new ShaderProgram(vertexShader, fragmentShader);
		m_waterShader = new ShaderProgram(vertexShader, fragmentShader2);
		m_matrix = new Matrix4();
		m_waterShader.setUniformMatrix("u_projTrans", m_matrix);
		PolygonShape shape = (PolygonShape) body.getFixtureList().get(0).getShape();
		ArrayList<Vector2> vertices = new ArrayList<Vector2>();
		for(int i = 0; i < shape.getVertexCount(); i++){
			Vector2 vertex = new Vector2();
			shape.getVertex(i, vertex);
			vertices.add(vertex);
		}
		m_waterMesh = createQuad(vertices.get(3).x * 100, vertices.get(3).y* 100, 
								vertices.get(0).x* 100, vertices.get(0).y* 100, 
								vertices.get(1).x* 100, vertices.get(1).y* 160,
								vertices.get(2).x* 100, vertices.get(2).y* 160);
	}
	
	float time;
	float xPosition = 0;
	Vector3 axis = new Vector3(1, 0, 1).nor();
	public void render(SpriteBatch batch, OrthographicCamera camera, SpriteComponent sprite){
		float dt = Gdx.graphics.getDeltaTime();
		time += dt;
		float angle = time * (2 * MathUtils.PI);
		if (angle > (2 * MathUtils.PI))
			angle -= (2 * MathUtils.PI);
		
		float aspect = camera.viewportWidth / (float)camera.viewportHeight;
		//projection.setToProjection(camera.near, camera.far, 100f, aspect);
		projection.set(camera.projection);
		//Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		System.out.println("camera "+projection);
		view.setToTranslation(-camera.position.x, -camera.position.y, -2.0f);
		//model.setToRotation(axis, angle);
		System.out.println(Converters.ToBox(sprite.getPosition().x) / 10 + " y "+Converters.ToBox(sprite.getPosition().y));
		model.setToTranslation(sprite.getPosition().x, sprite.getPosition().y, 0);
		//model.setToTranslation(Converters.ToBox(camera.position.x) / 10 + Converters.ToBox(sprite.getPosition().x) / 100, 0, 0);
		combined.set(projection).mul(view).mul(model);
		
		batch.setShader(m_waterShader);
		batch.begin();
		m_waterShader.setUniformMatrix("u_mvpMatrix", combined);
		sprite.draw(batch);
		batch.end();
		
		//RENDER WATER
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		sprite.getTexture().bind(1);
		m_displacementTexture.bind(2);
		batch.setShader(null);
		m_shader.begin();
		m_shader.setUniformMatrix("u_mvpMatrix", combined);

		m_shader.setUniformi("u_texture", 1);
		m_shader.setUniformi("u_texture2", 2);
		m_shader.setUniformf("timedelta", -angle * 2 );
		m_waterMesh.render(m_shader, GL20.GL_TRIANGLE_FAN);
		m_shader.end();
		
		m_displacementTexture.bind(0);
		m_displacementTexture.dispose();
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
		m_displacementTexture.dispose();
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub

	}

}
