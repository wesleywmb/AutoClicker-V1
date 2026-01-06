# AutoClicker-V1

Autoclicker com hotkey global para Windows.

## Requisitos

- **Windows 10/11**
- **Java 11+** no PATH

## Uso

### Iniciar o AutoClicker
```bat
START.bat
```
Compila automaticamente e executa o app.

### Criar atalho na Área de Trabalho
```bat
tools\create-shortcut-simple.bat
```
Cria um atalho na sua Área de Trabalho para executar o app facilmente.

### Ou via tools/
```bat
tools\start-simple.bat
```

### Build manual
```bat
tools\build.bat
```

## Hotkey

Padrão: **F6** (ativa/desativa o autoclicker - funciona minimizado)

## Estrutura

```
AutoClicker-V1/
├── START.bat         # Executar o app (simples)
├── src/main/java/    # Código-fonte Java
├── tools/            # Scripts auxiliares
├── lib/              # Dependências
└── build/            # JAR compilado
```

## Build com Maven

```bash
mvn clean package
java -jar target/autoclicker-v1.jar
```

