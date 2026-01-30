# AutoClicker Pro

Aplicativo de clique automático para Windows e recursos de automação.

## Funcionalidades

- **Controle preciso de intervalo**: Defina intervalos de 0.001 a 60 segundos entre cliques
- **Seleção de botão do mouse**: Botão esquerdo, direito ou do meio
- **Tipos de clique**: Clique simples ou duplo
- **Modos de repetição**: Infinito ou limitado (1 a 999.999 cliques)
- **Tecla de atalho global**: Tecla de ativação personalizável (padrão: F6)
- **Operação em segundo plano**: Funciona com a janela minimizada
- **Feedback**: Contador de cliques

## Requisitos do Sistema

- Windows 10/11
- Java 11 ou superior
- Maven (opcional, para compilação manual)

## Início Rápido

### 1: Compilar Manualmente
```
tools\compilar.bat
```
Compila o projeto e gera o arquivo JAR executável.

### Opção 2: Executar
```
tools\iniciar.bat
```
Este script baixa automaticamente as dependências, compila se necessário e executa o aplicativo.

### Opção 3: Criar Atalho na Área de Trabalho
```
tools\criar-atalho.bat
```
Cria um atalho `AutoClicker-Pro.bat` na área de trabalho para acesso rápido.

## Compilando a partir do Código-Fonte

### Usando Maven
```bash
mvn clean package
java -jar target/autoclicker-pro.jar
```

### Usando Scripts de Compilação
O projeto inclui scripts automatizados que gerenciam as dependências:
- `tools\compilar.bat` - Compila o projeto
- `tools\iniciar.bat` - Compila (se necessário) e executa o aplicativo

## Dependências

- **JNativeHook 2.2.2**: Listeners globais de teclado/mouse
- **FlatLaf 3.2.5**: Para aplicações Java Swing

---

## Sugestões e melhorias são bem-vindas.