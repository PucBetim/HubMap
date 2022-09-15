import { block, position } from './../models/map';
import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import html2canvas from 'html2canvas';

@Component({
  selector: 'export-image',
  templateUrl: './export-image.component.html',
  styleUrls: ['./export-image.component.scss']
})
export class ExportImageComponent implements OnInit {

  @ViewChild('canvas') canvas: ElementRef;
  @ViewChild('downloadLink') downloadLink: ElementRef;

  public blocks: block[] = [];
  public closesPoint = new position;
  public farestPoint = new position;
  public width: number = 0;
  public height: number = 0;

  constructor(public dialogRef: MatDialogRef<ExportImageComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit(): void {
    this.blocks = JSON.parse(JSON.stringify(this.data.blocks));

    this.getClosestFartest(this.blocks);
    this.repositionBlock(this.blocks)
    this.width = this.farestPoint.x - this.closesPoint.x + 60;
    this.height = this.farestPoint.y - this.closesPoint.y + 60;

  }

  downloadImage() {
    html2canvas(this.canvas.nativeElement).then(canvas => {
      this.canvas.nativeElement.src = canvas.toDataURL();
      this.downloadLink.nativeElement.href = canvas.toDataURL('image/png');
      this.downloadLink.nativeElement.download = 'mapa-mental.png';
      this.downloadLink.nativeElement.click();
    });
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
      b.position.x -= this.closesPoint.x - 30;
      b.position.y -= this.closesPoint.y - 30;

      this.repositionBlock(b.blocks);
    });
    return;
  }
}
