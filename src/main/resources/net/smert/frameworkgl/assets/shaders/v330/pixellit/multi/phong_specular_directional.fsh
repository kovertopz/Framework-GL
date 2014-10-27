#version 330

////////////////////////////////////////////////////////////////////////////////
// http://www.glprogramming.com/red/chapter05.html                            //
//                                                                            //
// color = (matEmission + globalAmbient * matAmbient) +                       //
//         AttenuationFactor( 1.0 / ( Kc + Kl*d + Kq*d^2 ) ) *                //
//         [ (lightAmbient * matAmbient) +                                    //
//           (max(N.L,0) * lightDiffuse * matDiffuse) +                       //
//           (max(N.H,0)^matShininess * lightSpecular * matSpecular) ]        //
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// Constants                                                                  //
////////////////////////////////////////////////////////////////////////////////
#define MAX_LIGHTS 32

////////////////////////////////////////////////////////////////////////////////
// In/Out Variables                                                           //
////////////////////////////////////////////////////////////////////////////////
in vec3 pass_Normal;
in vec4 pass_Vertex;
in vec4 pass_Color;
in vec4 pass_TexCoord0;
out vec4 out_Color;

////////////////////////////////////////////////////////////////////////////////
// Default Uniforms                                                           //
////////////////////////////////////////////////////////////////////////////////
uniform float uTextureFlag = 0.0;
uniform sampler2D uTexture0; // Diffuse texture

////////////////////////////////////////////////////////////////////////////////
// Structs                                                                    //
////////////////////////////////////////////////////////////////////////////////
struct CustomLightSourceParameters
{
    float constantAttenuation;
    float linearAttenuation;
    float quadraticAttenuation;
    float radius;
    float spotInnerCutoffCos;
    float spotOuterCutoffCos;
    float spotExponent;
    vec3 spotEyeDirection;
    vec4 ambient;
    vec4 diffuse;
    vec4 eyePosition;
    vec4 specular;
};

struct CustomMaterialParameters
{
    float shininess;
    vec4 ambient;
    vec4 diffuse;
    vec4 emission;
    vec4 specular;
};

////////////////////////////////////////////////////////////////////////////////
// Uniforms                                                                   //
////////////////////////////////////////////////////////////////////////////////
uniform int uNumberOfLights = 0;
uniform CustomLightSourceParameters uLights[MAX_LIGHTS];
uniform CustomMaterialParameters uMaterialLight;
uniform vec4 uGlobalAmbientLight;

////////////////////////////////////////////////////////////////////////////////
// Main                                                                       //
////////////////////////////////////////////////////////////////////////////////
void main(void)
{
    // Most tutorials call eyeVertexViewDir just "eyeDir" or "eyeViewDir". Both names
    // are very misleading to me since the camera eye direction comes to mind first.
    vec3 eyeVertexViewDir = normalize(-pass_Vertex.xyz);

    // Calculate emission and global ambient light
    vec4 emissionAmbient = uMaterialLight.emission + (uGlobalAmbientLight * uMaterialLight.ambient);

    // No light by default
    vec4 lightAmbient = vec4(0.0);
    vec4 lightDiffuse = vec4(0.0);
    vec4 lightSpecular = vec4(0.0);

    for ( int i = 0; i < uNumberOfLights; i++ )
    {
        // Calculate the light direction
        vec3 eyeLightDir = normalize(uLights[i].eyePosition.xyz);

        // Calculate ambient
        lightAmbient += uLights[i].ambient * uMaterialLight.ambient;

        // Calculate lambert term
        float NdotL = max(dot(pass_Normal, eyeLightDir), 0.0);

        // Calculate diffuse
        vec4 textureColor = texture(uTexture0, pass_TexCoord0.st);
        vec4 color = mix(pass_Color, pass_Color * textureColor, uTextureFlag); // Mix interpolated color and texture
        lightDiffuse += NdotL * (uLights[i].diffuse * uMaterialLight.diffuse * color);

        // Calculate specular
        if ( NdotL > 0.000001 )
        {
            vec3 eyeReflectLightDir = reflect(-eyeLightDir, pass_Normal);
            float RdotE = max(dot(eyeReflectLightDir, eyeVertexViewDir), 0.0);
            lightSpecular += pow(RdotE, uMaterialLight.shininess) * (uLights[i].specular * uMaterialLight.specular);
        }
    }

    out_Color = emissionAmbient + (lightAmbient + lightDiffuse + lightSpecular);
}