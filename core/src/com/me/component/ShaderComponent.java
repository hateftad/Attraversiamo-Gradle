package com.me.component;

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


public class ShaderComponent extends BaseComponent {
	
	private Texture m_displacementTexture;
	private Matrix4 m_matrix;
	private ShaderProgram m_shader;
	private ShaderProgram m_waterShader;
	private Mesh m_waterMesh;
	private String vertexShader;
	private String fragmentShader;
	private String fragmentShader2;
	
	public ShaderComponent(String extraTexture){
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
		
		m_waterMesh = createQuad(-0.6f, -0.6f, 0.6f, -0.6f, 0.6f, -0.3f, -0.6f, -0.3f);
	}
	
	float time;
	
	public void render(SpriteBatch batch, OrthographicCamera camera, SpriteComponent sprite){
		float dt = Gdx.graphics.getDeltaTime();
		time += dt;
		float angle = time * (2 * MathUtils.PI);
		if (angle > (2 * MathUtils.PI))
			angle -= (2 * MathUtils.PI);

		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setShader(m_waterShader);
		batch.begin();
		m_waterShader.setUniformMatrix("u_worldView", camera.combined);
		sprite.draw(batch);
		batch.end();
		
		//RENDER WATER
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		sprite.getTexture().bind(1);
		m_displacementTexture.bind(2);
		batch.setShader(null);
		m_shader.begin();
		m_shader.setUniformMatrix("u_worldView",  m_matrix);

		m_shader.setUniformi("u_texture", 1);
		m_shader.setUniformi("u_texture2", 2);
		m_shader.setUniformf("timedelta", -angle * 2);
		m_waterMesh.render(m_shader, GL20.GL_TRIANGLE_FAN);
		m_shader.end();
		//sprite.getTexture().bind(0);
		m_displacementTexture.bind(0);
		//batch.disableBlending();
		
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
