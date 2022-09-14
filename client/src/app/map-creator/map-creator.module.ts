import { SharedModule } from './../core/shared/shared.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipDefaultOptions, MatTooltipModule, MAT_TOOLTIP_DEFAULT_OPTIONS } from '@angular/material/tooltip';
import { MatMenuModule } from '@angular/material/menu';
import { FormsModule } from '@angular/forms';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';

import { CreationComponent } from './main/creation.component';
import { CreatorRoutes } from './map-creator.routing';
import { StyleEditorComponent } from './style-editor/style-editor.component';
import { BlockComponent } from './block/block.component';
import { TextareaComponent } from './textarea/textarea.component';
import { DragScrollModule } from 'ngx-drag-scroll';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { LineSvgComponent } from './line-svg/line-svg.component';
import { ExportImageComponent } from './export-image/export-image.component';
import { VisualBlockComponent } from './export-image/visual-block/visual-block.component';
import { TextDivComponent } from './export-image/text-div/text-div.component';

export const OtherOptions: MatTooltipDefaultOptions = {
  showDelay: 0,
  hideDelay: 0,
  touchGestures: 'auto',
  position: 'below',
  touchendHideDelay: 0,
  disableTooltipInteractivity: true,
}

@NgModule({
  declarations: [
    CreationComponent,
    StyleEditorComponent,
    BlockComponent,
    TextareaComponent,
    ConfirmDialogComponent,
    LineSvgComponent,
    ExportImageComponent,
    VisualBlockComponent,
    TextDivComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(CreatorRoutes),
    DragDropModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    FormsModule,
    MatMenuModule,
    MatButtonToggleModule,
    MatSelectModule,
    DragScrollModule,
    MatDialogModule,
    MatCardModule,
    SharedModule
  ],
  providers: [
    { provide: MAT_TOOLTIP_DEFAULT_OPTIONS, useValue: OtherOptions }
  ]
})
export class MapCreatorModule { }