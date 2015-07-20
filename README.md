Framework-GL
===============

Simple OpenGL framework for Java which uses LWJGL 3.0 as the backend. While
the framework has support for compatibility mode the emphasis is on targeting
modern practices using OpenGL 3.3.

Framework-GL has the following features :

- 3D sound system through OpenAL. Playback supports (AIFF, AU, MP3,
  OGG and WAV). MIDI playback is available through Java Sound API.
- Basic rendering with immediate mode, display lists, vertex arrays,
  vertex buffer objects (dynamic & interleaved) using OpenGL 1.X.
- Basic rendering with vertex arrays, vertex buffer objects (dynamic
  & interleaved) and shaders using OpenGL 2.X.
- Basic rendering with vertex array objects and shaders using OpenGL
  3.X.
- Bitmap font rendering (using Awt or Angel Code)
- Builders (FBOs, shaders & textures)
- Built-in shaders (Diffuse, Blinn-Phong and Phong either pixel or
  vertex lit)
- Collision detection using AABBs including a dynamic AABB tree
  for broadphase.
- Cube maps
- Dynamic mesh builder for capsules, cones, cubes, cylinders,
  frustums, grids, pyramids, quads, spheres and toriods.
- Frustum culling in clip space.
- Math library (AABBs, matrices, quaternions and vectors)
- Model loading (OBJ & MTL)
- OpenGL constants have been organized to be more useful.
- OpenGL helpers for display lists, frame buffer objects, legacy
  rendering, render buffer objects, shaders, shader uniforms, textures,
  vertex arrays and vertex buffer objects.
- Texture loading (BMP, GIF, JPG, PNG, TGA, and TIFF)
- Multi-platform (Windows, Linux, Mac OS X)
- Examples

Github : https://github.com/kovertopz/Framework-GL  
Author : Jason Sorensen
