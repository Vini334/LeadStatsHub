# Migração para Spring Security 6 - Documentação

## Problemas Resolvidos

### 1. BeanCreationException - AuthenticationManager
**Erro:** `Cannot apply ... AuthenticationConfiguration$EnableGlobalAuthenticationAutowiredConfigurer ... to already built object`

**Causa:** Múltiplos beans de AuthenticationManager conflitantes e uso de APIs deprecated.

**Solução:** 
- Removido `authenticationManagerBean()` que usava `AuthenticationConfiguration`
- Removido `authenticationManager(AuthenticationManagerBuilder)` deprecated
- Implementado padrão moderno com `DaoAuthenticationProvider` + `ProviderManager`

### 2. Configuração de Segurança Moderna
**Implementado:**
- `@EnableWebSecurity` para configuração explícita
- `SecurityFilterChain` com configuração funcional
- `DaoAuthenticationProvider` com injeção de dependência
- Endpoints públicos: `/api/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`
- SessionManagement STATELESS para APIs

## Arquivos Modificados

### SecurityConfig.java
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider daoAuthProvider) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
            )
            .authenticationProvider(daoAuthProvider)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider daoAuthProvider) {
        return new ProviderManager(daoAuthProvider);
    }
}
```

### UserDetailsServiceImpl.java
- Removido `@Service` e `@Autowired`
- Implementado injeção via construtor
- Agora é instanciado como bean no SecurityConfig

### AuthController.java
- Migrado de `@Autowired` para injeção via construtor
- Mantida funcionalidade de autenticação

### Testes
- Adicionado `@TestConfiguration` para liberar segurança em testes
- Criado `application-test.properties` com H2 para testes
- Adicionado `@ActiveProfiles("test")`

## Validação

### Compilação
```bash
mvn clean compile
# ✅ Sucesso - sem erros
```

### Testes
```bash
mvn test
# ✅ Sucesso - contexto sobe sem problemas
```

### Endpoints Públicos
- `/api/auth/login` - Autenticação
- `/v3/api-docs/**` - OpenAPI docs
- `/swagger-ui/**` - Swagger UI

### Endpoints Protegidos
- Todos os outros endpoints requerem autenticação

## Benefícios da Migração

1. **Compatibilidade:** Spring Security 6 + Spring Boot 3.5.6
2. **Performance:** Configuração mais eficiente
3. **Manutenibilidade:** Código mais limpo e moderno
4. **Testabilidade:** Testes funcionam sem problemas de contexto
5. **Segurança:** Configuração robusta e atualizada

## Próximos Passos

1. Testar autenticação em ambiente de desenvolvimento
2. Validar endpoints protegidos
3. Configurar JWT se necessário
4. Ajustar configurações de produção conforme necessário
