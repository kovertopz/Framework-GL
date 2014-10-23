#version 120

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
varying vec3 pass_Normal;
varying vec4 pass_Vertex;
varying vec4 pass_Color;
varying vec4 pass_TexCoord0;

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
        vec3 eyeLightDir = uLights[i].eyePosition.xyz - pass_Vertex.xyz;
        float dist = length(eyeLightDir);
        eyeLightDir = normalize(eyeLightDir);

        // No attenuation for a directional light
        float attenuationFactor = 1.0 / (uLights[i].constantAttenuation
                                       + uLights[i].linearAttenuation * dist
                                       + uLights[i].quadraticAttenuation * dist * dist);
        attenuationFactor *= clamp(1.0 - (dist * dist) / (uLights[i].radius * uLights[i].radius), 0.0, 1.0);

        // Skip light if it won't matter
        if ( attenuationFactor <= 0.0 )
        {
            continue;
        }

        // Calculate ambient
        lightAmbient += uLights[i].ambient * uMaterialLight.ambient * attenuationFactor;

        // Calculate lambert term
        float NdotL = max(dot(pass_Normal, eyeLightDir), 0.0);

        // Calculate diffuse
        vec4 textureColor = texture2D(uTexture0, pass_TexCoord0.st);
        vec4 color = mix(pass_Color, pass_Color * textureColor, uTextureFlag); // Mix interpolated color and texture
        lightDiffuse += NdotL * (uLights[i].diffuse * uMaterialLight.diffuse * color) * attenuationFactor;

        // Calculate specular
        if ( NdotL > 0.000001 )
        {
            vec3 eyeReflectLightDir = reflect(-eyeLightDir, pass_Normal);
            float RdotE = max(dot(eyeReflectLightDir, eyeVertexViewDir), 0.0);
            lightSpecular += pow(RdotE, uMaterialLight.shininess) * (uLights[i].specular * uMaterialLight.specular)
                             * attenuationFactor;
        }
    }

    gl_FragColor = emissionAmbient + (lightAmbient + lightDiffuse + lightSpecular);
}