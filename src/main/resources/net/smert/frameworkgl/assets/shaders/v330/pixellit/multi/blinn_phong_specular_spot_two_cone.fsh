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

        // Calculate cone's light influence
        if ( uLights[i].spotOuterCutoffCos != -1.0 )
        {
            float coneCosAngle = dot(-eyeLightDir, normalize(uLights[i].spotEyeDirection));
            float edge0 = uLights[i].spotOuterCutoffCos;
            float edge1 = uLights[i].spotOuterCutoffCos + (1.0 - uLights[i].spotInnerCutoffCos);
            float t = clamp((coneCosAngle - edge0) / (edge1 - edge0), 0.0, 1.0);
            float coneEffect = t * t * (3.0 - 2.0 * t);
            attenuationFactor *= coneEffect;
        }

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
        vec4 textureColor = texture(uTexture0, pass_TexCoord0.st);
        vec4 color = mix(pass_Color, pass_Color * textureColor, uTextureFlag); // Mix interpolated color and texture
        lightDiffuse += NdotL * (uLights[i].diffuse * uMaterialLight.diffuse * color) * attenuationFactor;

        // Calculate specular
        if ( NdotL > 0.000001 )
        {
            vec3 halfVector = normalize(eyeLightDir - pass_Vertex.xyz);
            float NdotHV = max(dot(pass_Normal, halfVector), 0.0);
            lightSpecular += pow(NdotHV, uMaterialLight.shininess) * (uLights[i].specular * uMaterialLight.specular)
                             * attenuationFactor;
        }
    }

    out_Color = emissionAmbient + (lightAmbient + lightDiffuse + lightSpecular);
}