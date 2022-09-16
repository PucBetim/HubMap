import { block, position } from '../../models/map';
import { AfterViewInit, Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import html2canvas from 'html2canvas';

@Component({
  selector: 'visual-canvas',
  templateUrl: './visual-canvas.component.html',
  styleUrls: ['./visual-canvas.component.scss']
})
export class VisualCanvasComponent implements OnInit, AfterViewInit {

  @ViewChild('canvas') canvas: ElementRef;
  @ViewChild('showcase') showcase: ElementRef;
  @ViewChild('downloadLink') downloadLink: ElementRef;

  public carregando: boolean = false;
  public blocks: block[] = [];
  public closesPoint = new position;
  public farestPoint = new position;
  public width: number = 0;
  public height: number = 0;
  public spacingImageBorder: number = 60;

  constructor(public dialogRef: MatDialogRef<VisualCanvasComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
  }


  ngOnInit(): void {
    this.blocks = JSON.parse(JSON.stringify(this.data.blocks));

    this.getClosestFartest(this.blocks);
    this.repositionBlock(this.blocks)
    this.width = this.farestPoint.x - this.closesPoint.x + this.spacingImageBorder + 14; // 14 = Considerar padding e borda
    this.height = this.farestPoint.y - this.closesPoint.y + this.spacingImageBorder + 14; // 14 = Considerar padding e borda
  }

  ngAfterViewInit(): void {
    this.generateImage()
  }

  generateImage() {
    this.carregando = true;
    setTimeout(() => {
      html2canvas(this.canvas.nativeElement).then(canvas => {
        this.showcase.nativeElement.src = canvas.toDataURL();
        this.canvas.nativeElement.src = canvas.toDataURL();
        this.downloadLink.nativeElement.href = canvas.toDataURL('image/png');
        this.downloadLink.nativeElement.download = 'mapa-mental.png';
      });
      this.carregando = false
    }, 1000)
  }

  downloadImage() {
    this.downloadLink.nativeElement.click();
  }


  getClosestFartest(blocks: block[]) {

    blocks.forEach(b => {
      //Closest
      if (!this.closesPoint.x) { this.closesPoint.x = b.position.x }
      else
        this.closesPoint.x = this.closesPoint.x < b.position.x ? this.closesPoint.x : b.position.x;

      if (!this.closesPoint.y) { this.closesPoint.y = b.position.y }
      else
        this.closesPoint.y = this.closesPoint.y < b.position.y ? this.closesPoint.y : b.position.y;
      //Closest end

      //Farest
      if (!this.farestPoint.x) { this.farestPoint.x = (b.position.x + b.size.width) }
      else
        this.farestPoint.x = this.farestPoint.x > (b.position.x + b.size.width) ? this.farestPoint.x : (b.position.x + b.size.width);

      if (!this.farestPoint.y) { this.farestPoint.y = (b.position.y + b.size.height) }
      else
        this.farestPoint.y = this.farestPoint.y > (b.position.y + b.size.height) ? this.farestPoint.y : (b.position.y + b.size.height);
      //Farest End

      this.getClosestFartest(b.blocks)
    });
    return;
  }

  repositionBlock(blocks: block[]) {
    blocks.forEach(b => {
      b.position.x -= this.closesPoint.x - this.spacingImageBorder / 2;
      b.position.y -= this.closesPoint.y - this.spacingImageBorder / 2;
      this.repositionBlock(b.blocks);
      return;
    });
  }
}
