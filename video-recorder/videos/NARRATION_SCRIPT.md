# AI Logistics Hub - Video Narration Script

This script is designed to be recorded as a voiceover for the `final_showcase.webm` video. The timing is synchronized with the visual transitions in the merged recording.

---

### **Automatización de Audio y Pacing**

El video final utiliza **FFmpeg** para sincronizar automáticamente los bloques de audio. 

- **Ajuste de Velocidad**: Los bloques de audio que eran demasiado largos para su escena han sido acelerados automáticamente mediante el filtro `atempo`. Esto elimina los solapamientos de voces y asegura que cada narración termine antes de que comience la siguiente sección.
- **Sincronización**: Los tiempos de inicio (0s, 10s, 20s, 30s, 45s, 55s) están fijados en el script de mezcla.

---

#### **Bloque 1 (Tiempo: 0:00)**
> "Logistics operations are often buried in unstructured data—messy emails and invoices that cause critical delays. Today, we’re automating that chaos."

#### **Bloque 2 (Tiempo: 0:10)**
> "This is the AI Logistics Hub. With one click, our pipeline uses Groq AI to extract structured entities from raw text. Watch as 5 complex scenarios are processed live."

#### **Bloque 3 (Tiempo: 0:20)**
> "The system intelligently identifies company names, dates, and amounts, while also flagging status and urgency. Notice the color-coded badges providing instant operational visibility."

#### **Bloque 4 (Tiempo: 0:30)**
> "Let's submit a custom critical event. A port blockage in Rotterdam. By submitting this raw text via our API, the AI instantly structures the data, identifying the $125,000 value at risk."

#### **Bloque 5 (Tiempo: 0:45)**
> "The record appears instantly. Slack and email alerts have already been dispatched. This is professional-grade automation, built with Spring Boot 3 and a clean, layered architecture."

#### **Bloque 6 (Tiempo: 0:55)**
> "Built for performance. Built for scale. This is the AI Logistics Automation Hub."

---

### **Consejos Pro en ClipChamp:**
- **Voz Recomendada**: Busca voces con etiquetas como "Professional" o "News" para un tono serio.
- **Sincronización**: Usa el zoom de la línea de tiempo para que el audio coincida exactamente con las transiciones del video.
- **Exportación**: Usa resolución 1080p para que el código fuente se vea legible.
