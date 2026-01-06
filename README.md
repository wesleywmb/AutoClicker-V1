# AutoClicker-V1

Autoclicker com hotkey global para Windows.

## Requisitos

- **Windows 10/11**
- **Java 11+** no PATH

## Uso

### Iniciar o AutoClicker
```bat
tools\START.bat
```

### Criar atalho na Área de Trabalho
```bat
tools\create-shortcut-simple.bat
```
Cria um arquivo `AutoClicker-V1.bat` na Área de Trabalho.

### Build manual
```bat
tools\build.bat
```

## Hotkey

Padrão: **F6** (ativa/desativa - funciona minimizado)

## Estrutura

```
AutoClicker-V1/
├── tools/
│   ├── START.bat                  # Executar o app
│   ├── build.bat                  # Compilar JAR
│   ├── start-simple.bat           # Script interno
│   └── create-shortcut-simple.bat # Criar atalho desktop
├── src/main/java/                 # Código-fonte
├── lib/                           # Dependências (auto-download)
├── .gitignore
├── pom.xml
└── README.md
```

## Build com Maven

```bash
mvn clean package
java -jar target/autoclicker-v1.jar
```

