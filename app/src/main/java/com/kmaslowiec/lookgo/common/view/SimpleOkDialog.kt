package com.kmaslowiec.lookgo.common.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.kmaslowiec.lookgo.R
import com.kmaslowiec.lookgo.common.utils.showWords

@Composable
fun SimpleOkDialog(
    title: String,
    content: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title)
        },
        text = {
            Text(content)
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.button_ok))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SimpleOkDialogPreview() {
    SimpleOkDialog(
        title = "Title",
        content = LoremIpsum().showWords(30)
    ) { }
}
