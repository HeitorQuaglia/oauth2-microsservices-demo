package edu.heitorquaglia.post.api.responses

import edu.heitorquaglia.post.client.UserResponse

class EditorResponse(val id: Long,val name: String) {
    companion object {
        fun of(editor: UserResponse): EditorResponse {
            return EditorResponse(editor.id!!, editor.name!!)
        }

        fun anonymousEditor(id: Long): EditorResponse {
            return EditorResponse(id, "Anônimo")
        }
    }
}