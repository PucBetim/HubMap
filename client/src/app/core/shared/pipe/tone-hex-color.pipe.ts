import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'toneHexColor'
})
export class ToneHexColorPipe implements PipeTransform {
    transform(color: string, percent: number): any {
        var usePound = false;
        if (color[0] == "#") {
            color = color.slice(1);
            usePound = true;
        }

        var num = parseInt(color, 16),
            amt = Math.round(2.55 * percent),
            R = (num >> 16) + amt,
            B = (num >> 8 & 0x00FF) + amt,
            G = (num & 0x0000FF) + amt;

        return (usePound ? "#" : "") + (0x1000000 + (R < 255 ? R < 1 ? 0 : R : 255) * 0x10000 + (B < 255 ? B < 1 ? 0 : B : 255) * 0x100 + (G < 255 ? G < 1 ? 0 : G : 255)).toString(16).slice(1);
    }
}