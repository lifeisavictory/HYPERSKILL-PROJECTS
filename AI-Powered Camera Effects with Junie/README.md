# AI-Powered Camera Effects with Junie – Dokumentace projektu

Tento projekt je interaktivní webová aplikace pro aplikaci vizuálních efektů na video z webkamery v reálném čase. Aplikace nabízí různé barevné filtry inspirované Instagramem a pokročilé grafické efekty jako pixelace a halftone (autotypie).

> **Poznámka:** Tato aplikace i její dokumentace byly kompletně generovány umělou inteligencí (AI), konkrétně agentem Junie.

## 🚀 Funkce

- **Streamování videa v reálném čase**: Přístup k webkameře uživatele a zobrazení obrazu.
- **Barevné filtry**: Sada přednastavených filtrů (Normal, Clarendon, Gingham, Moon, Lark, Reyes, Juno, Willow, Valencia, X-Pro II).
- **Pixelace**: Možnost rozkostičkování obrazu s nastavitelnou velikostí pixelů.
- **Halftone efekt**: Efekt tiskového rastru (tečkování) s nastavitelným rozestupem bodů.
- **Responzivní design**: Rozhraní přizpůsobené pro různé velikosti obrazovek.

## 🛠 Technologie

- **HTML5**: Struktura aplikace a video/canvas elementy.
- **CSS3**: Styling rozhraní a aplikace základních vizuálních filtrů.
- **JavaScript (Vanilla)**: Logika aplikace, ovládání webkamery a vykreslování pokročilých efektů přes Canvas API.
- **Canvas API**: Použito pro manipulaci s pixely v reálném čase (pixelace, halftone).

## 📂 Struktura souborů

- `index.html`: Hlavní struktura stránky s kontejnery pro video a ovládací prvky.
- `style.css`: Definice vzhledu, rozvržení a animací.
- `script.js`: Jádro aplikace – inicializace kamery, renderovací smyčka a implementace efektů.

## ⚙️ Jak to funguje

1. **Inicializace**: Po načtení stránky aplikace požádá o přístup k webkameře pomocí `navigator.mediaDevices.getUserMedia`.
2. **Renderovací smyčka**: Pomocí `requestAnimationFrame` se obraz z videa neustále překresluje na `canvas`.
3. **Aplikace filtrů**:
   - Základní filtry jsou aplikovány přímo přes CSS vlastnost `filter`.
   - **Pixelace**: Obraz je vykreslen ve zmenšeném měřítku na pomocné plátno a následně roztažen zpět bez vyhlazování.
   - **Halftone**: Jas obrazu je analyzován pixel po pixelu a nahrazen černými tečkami na bílém pozadí, jejichž velikost odpovídá tmavosti původního pixelu.

## 📖 Instalace a spuštění

Aplikace nevyžaduje žádnou instalaci ani build proces. Stačí otevřít soubor `index.html` v moderním webovém prohlížeči. Pro správnou funkci kamery je doporučeno spouštět aplikaci přes lokální server (např. VS Code Live Server nebo jednoduchý python server).

---
*Aplikace i dokumentace byly vytvořeny Junie AI (JetBrains).*
