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
in vec3 in_Normal;
in vec4 in_Vertex;
in vec4 in_Color;
in vec4 in_TexCoord0;
out vec4 pass_Color;
out vec4 pass_TexCoord0;

////////////////////////////////////////////////////////////////////////////////
// Default Uniforms                                                           //
////////////////////////////////////////////////////////////////////////////////
uniform mat3 uNormalMatrix;
uniform mat4 uProjectionViewModelMatrix;
uniform mat4 uViewModelMatrix;

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
uniform float uColorMaterialAmbient = 1.0;
uniform float uColorMaterialDiffuse = 1.0;
uniform float uColorMaterialEmission = 0.0;
uniform float uColorMaterialSpecular = 0.0;
uniform int uNumberOfLights = 0;
uniform CustomLightSourceParameters uLights[MAX_LIGHTS];
uniform CustomMaterialParameters uMaterialLight;
uniform vec4 uGlobalAmbientLight;

////////////////////////////////////////////////////////////////////////////////
// Main                                                                       //
////////////////////////////////////////////////////////////////////////////////
void main(void)
{
    vec4 matAmbient = mix(uMaterialLight.ambient, in_Color, uColorMaterialAmbient);
    vec4 matDiffuse = mix(uMaterialLight.diffuse, in_Color, uColorMaterialDiffuse);
    vec4 matEmission = mix(uMaterialLight.emission, in_Color, uColorMaterialEmission);
    vec4 matSpecular = mix(uMaterialLight.specular, in_Color, uColorMaterialSpecular);

    // Transform normal into eye space. uNormalMatrix is the transpose of the
    // inverse of the upper leftmost 3x3 of uViewModelMatrix.
    vec3 eyeNormal = normalize(uNormalMatrix * in_Normal);

    // Transform the vertex into eye space
    vec4 eyeVertex = uViewModelMatrix * in_Vertex;

    // Most tutorials call eyeVertexViewDir just "eyeDir" or "eyeViewDir". Both names
    // are very misleading to me since the camera eye direction comes to mind first.
    vec3 eyeVertexViewDir = normalize(-eyeVertex.xyz);

    // Calculate emission and global ambient light
    vec4 emissionAmbient = matEmission + (uGlobalAmbientLight * matAmbient);

    // No light by default
    vec4 lightAmbient = vec4(0.0);
    vec4 lightDiffuse = vec4(0.0);
    vec4 lightSpecular = vec4(0.0);

    for ( int i = 0; i < uNumberOfLights; i++ )
    {
        float attenuationFactor = 1.0;
        vec3 eyeLightDir;

        if ( uLights[i].eyePosition.w != 0.0 )
        {
            // Calculate the light direction
            eyeLightDir = uLights[i].eyePosition.xyz - eyeVertex.xyz;
            float dist = length(eyeLightDir);
            eyeLightDir = normalize(eyeLightDir);

            // No attenuation for a directional light
            attenuationFactor = 1.0 / (uLights[i].constantAttenuation
                                           + uLights[i].linearAttenuation * dist
                                           + uLights[i].quadraticAttenuation * dist * dist);
            attenuationFactor *= clamp(1.0 - (dist * dist) / (uLights[i].radius * uLights[i].radius), 0.0, 1.0);

            // Calculate cone's light influence
            if ( uLights[i].spotOuterCutoffCos != -1.0 )
            {
                float coneCosAngle = dot(-eyeLightDir, normalize(uLights[i].spotEyeDirection));
                float coneEffect;
                if ( ( coneCosAngle > 0.000001 ) && ( coneCosAngle >= uLights[i].spotOuterCutoffCos ) ) {
                    coneEffect = pow(coneCosAngle, uLights[i].spotExponent);
                } else {
                    coneEffect = 0.0;
                }
                attenuationFactor *= coneEffect;
            }
        }
        else
        {
            // Calculate the light direction
            eyeLightDir = normalize(uLights[i].eyePosition.xyz);
        }

        // Skip light if it won't matter
        if ( attenuationFactor <= 0.0 )
        {
            continue;
        }

        // Calculate ambient
        lightAmbient += uLights[i].ambient * matAmbient * attenuationFactor;

        // Calculate lambert term
        float NdotL = max(dot(eyeNormal, eyeLightDir), 0.0);

        // Calculate diffuse
        lightDiffuse += NdotL * (uLights[i].diffuse * matDiffuse) * attenuationFactor;

        // Calculate specular
        if ( NdotL > 0.000001 )
        {
            vec3 eyeReflectLightDir = reflect(-eyeLightDir, eyeNormal);
            float RdotE = max(dot(eyeReflectLightDir, eyeVertexViewDir), 0.0);
            lightSpecular += pow(RdotE, uMaterialLight.shininess) * (uLights[i].specular * matSpecular)
                             * attenuationFactor;
        }
    }

    pass_Color = emissionAmbient + (lightAmbient + lightDiffuse + lightSpecular);
    pass_TexCoord0 = in_TexCoord0;
    gl_Position = uProjectionViewModelMatrix * in_Vertex;
}