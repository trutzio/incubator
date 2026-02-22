# Installation tailwind

1. tailwind and tailwind-cli https://tailwindcss.com/docs/installation/tailwind-cli
    1. `npx @tailwindcss/cli -i ./src/input.css -o ./src/output.css --watch`
1. prettier tailwind plugin https://github.com/tailwindlabs/prettier-plugin-tailwindcss

# Corporate Design

## Farbe

1. Hauptfarbe hat ein Hue von `35` Grad (Orange)
1. Lightness wird nach diesem Array `[97.78, 93.56, 88.11, 82.67, 74.22, 64.78, 57.33, 46.89, 39.44, 32, 23.78]` abgestuft, dies entspricht Lightness Werten von `[95%, 90%, 80%, ... 20%, 10%, 5%]` siehe auch https://evilmartians.com/chronicles/better-dynamic-themes-in-tailwind-with-oklch-color-magic
1. Die Primary Farbe hat ein Lightness Wert von `0.32` also `10%`, siehe obiges Array, und wird für den Hintergrund im Dark Modus verwendet und für die Textfarbe im hellen Modus
2. Die Secondary Farbe hat ein Hue Wert von `35+180=215` Grad, ein Lightness Wert von `97.78%` also `95%`, siehe obiges Array, und wird für die Textfarbe im dunklen Modus und für den Hintergrund im hellen Modus verwendet
1. Chroma wird mit diesem Tool eingestellt https://oklch.com/
1. primary: oklch(0.32 0.112 35) #5f1300
1. Secondary Farbe: oklch(0.9778 0.0168 215) #ecfbff
1. Tailwind Farben mit https://coolors.co/tailwind/2f9f52


# Themen

1. Farben https://tailwindcss.com/docs/colors
    1. Farbtheorie oklch https://oklch.org/
    1. https://oklch.com/
    1. https://bottosson.github.io/posts/oklab/
    1. https://developer.mozilla.org/en-US/docs/Web/CSS/Reference/Values/color_value/oklch
    1. https://evilmartians.com/chronicles/better-dynamic-themes-in-tailwind-with-oklch-color-magic
1. Dark Modus https://tailwindcss.com/docs/dark-mode, siehe auch ./src/examples/dark.html
1. Font https://rsms.me/inter/
    1. https://fontjoy.com/
1. MarkDown
    1. https://marked.js.org/using_advanced#extensions
1. Python HTTP Server
    1. `python -m http.server`

