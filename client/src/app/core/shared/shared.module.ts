import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { DragDropModule } from '@angular/cdk/drag-drop';

import { DragScrollModule } from 'ngx-drag-scroll';
import { FormErrorComponent } from './form-error/form-error.component';
import { LoadingComponent } from './loading/loading.component';
import { ToneHexColorPipe } from './pipe/tone-hex-color.pipe';
import { LineSvgComponent } from './line-svg/line-svg.component';
import { DisplayCanvasComponent } from './display-map/display-canvas/display-canvas.component';
import { DisplayBlockComponent } from './display-map/display-block/display-block.component';
import { VisualBlockComponent } from './export-image/visual-block/visual-block.component';
import { VisualCanvasComponent } from './export-image/visual-canvas/visual-canvas.component';
import { VisualTextDivComponent } from './export-image/visual-text-div/visual-text-div.component';
import { MatMenuModule } from '@angular/material/menu';
import { MatDialogModule } from '@angular/material/dialog';
import { ContenteditableModel } from './contentEditableModule';

@NgModule({
  declarations: [
    FormErrorComponent,
    LoadingComponent,
    ToneHexColorPipe,
    LineSvgComponent,
    DisplayCanvasComponent,
    DisplayBlockComponent,
    VisualCanvasComponent,
    VisualBlockComponent,
    VisualTextDivComponent,
    ContenteditableModel
  ],
  imports: [
    CommonModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    DragDropModule,
    MatIconModule,
    DragScrollModule,
    MatMenuModule,
    MatDialogModule
  ],
  exports: [
    FormErrorComponent,
    LoadingComponent,
    MatIconModule,
    ToneHexColorPipe,
    LineSvgComponent,
    DisplayCanvasComponent,
    DisplayBlockComponent,
    VisualCanvasComponent,
    VisualBlockComponent,
    VisualTextDivComponent,
    ContenteditableModel
  ]
})
export class SharedModule { }
