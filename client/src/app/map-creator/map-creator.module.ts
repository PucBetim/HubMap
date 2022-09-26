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
import { MatSliderModule } from '@angular/material/slider';
import { MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { CreationComponent } from './main/creation.component';
import { CreatorRoutes } from './map-creator.routing';
import { StyleEditorComponent } from './style-editor/style-editor.component';
import { BlockComponent } from './block/block.component';
import { TextDivComponent } from './text-div/text-div.component';
import { DragScrollModule } from 'ngx-drag-scroll';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { VisualCanvasComponent } from './export-image/visual-canvas/visual-canvas.component';
import { VisualBlockComponent } from './export-image/visual-block/visual-block.component';
import { VisualTextDivComponent } from './export-image/visual-text-div/visual-text-div.component';
import { SharedModule } from './../core/shared/shared.module';
import { PendingChangesGuard } from '../core/services/guard.service';

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
    TextDivComponent,
    ConfirmDialogComponent,
    VisualCanvasComponent,
    VisualBlockComponent,
    VisualTextDivComponent,
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
    MatSliderModule,
    SharedModule,
    MatProgressSpinnerModule
  ],
  providers: [
    { provide: MAT_TOOLTIP_DEFAULT_OPTIONS, useValue: OtherOptions },
    PendingChangesGuard
  ]
})
export class MapCreatorModule { }
