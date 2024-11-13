<template>
  <div class="container">
    <div class="tiptap-container">
      <div v-if="editor" class="tiptap-buttons">
        <button @click="editor.chain().focus().toggleBold().run()"
          :disabled="!editor.can().chain().focus().toggleBold().run()"
          :class="{ 'is-active': editor.isActive('bold') }">
          Bold
        </button>
        <button @click="editor.chain().focus().toggleItalic().run()"
          :disabled="!editor.can().chain().focus().toggleItalic().run()"
          :class="{ 'is-active': editor.isActive('italic') }">
          Italic
        </button>
        <button @click="editor.chain().focus().toggleUnderline().run()"
          :disabled="!editor.can().chain().focus().toggleUnderline().run()"
          :class="{ 'is-active': editor.isActive('underline') }">
          Underline
        </button>
        <button @click="editor.chain().focus().toggleHeading({ level: 1 }).run()"
          :class="{ 'is-active': editor.isActive('heading', { level: 1 }) }">
          H1
        </button>
        <button @click="editor.chain().focus().toggleHeading({ level: 2 }).run()"
          :class="{ 'is-active': editor.isActive('heading', { level: 2 }) }">
          H2
        </button>
        <button @click="editor.chain().focus().toggleHeading({ level: 3 }).run()"
          :class="{ 'is-active': editor.isActive('heading', { level: 3 }) }">
          H3
        </button>
        <button @click="editor.chain().focus().toggleBulletList().run()"
          :class="{ 'is-active': editor.isActive('bulletList') }">
          Toggle bullet list
        </button>
        <button @click="editor.chain().focus().toggleOrderedList().run()"
          :class="{ 'is-active': editor.isActive('orderedList') }">
          Toggle ordered list
        </button>
        <button @click="editor.chain().focus().insertTable({ rows: 3, cols: 3, withHeaderRow: true }).run()">
          Insert table
        </button>
        <button @click="editor.chain().focus().addColumnAfter().run()">
          Add column after
        </button>
        <button @click="editor.chain().focus().deleteColumn().run()">
          Delete column
        </button>
        <button @click="editor.chain().focus().addRowAfter().run()">
          Add row after
        </button>
        <button @click="editor.chain().focus().deleteRow().run()">
          Delete row
        </button>
        <button @click="editor.chain().focus().deleteTable().run()">
          Delete table
        </button>
        <button @click="editor.chain().focus().mergeCells().run()">
          Merge cells
        </button>
        <button @click="editor.chain().focus().splitCell().run()">
          Split cell
        </button>
      </div>
      <editor-content :editor="editor" />
    </div>
  </div>
</template>

<script>
import StarterKit from '@tiptap/starter-kit'
import Heading from '@tiptap/extension-heading'
import BulletList from '@tiptap/extension-bullet-list'
import OrderedList from '@tiptap/extension-ordered-list'
import Paragraph from '@tiptap/extension-paragraph'
import Text from '@tiptap/extension-text'
import Gapcursor from '@tiptap/extension-gapcursor'
import Table from '@tiptap/extension-table'
import TableCell from '@tiptap/extension-table-cell'
import TableHeader from '@tiptap/extension-table-header'
import TableRow from '@tiptap/extension-table-row'
import Bold from '@tiptap/extension-bold'
import Italic from '@tiptap/extension-italic'
import Underline from '@tiptap/extension-underline'

import { useEditor, EditorContent } from '@tiptap/vue-3'

export default {
  components: {
    EditorContent,
  },

  setup() {
    const editor = useEditor({
      content: "",
      extensions: [
        Text,
        Paragraph,
        Bold,
        Italic,
        Underline,
        BulletList,
        OrderedList,
        StarterKit,
        Gapcursor,
        Table.configure({
          resizable: true,
        }),
        TableRow,
        TableHeader,
        TableCell,
        Heading.configure({
          levels: [1, 2, 3],
        }),
      ],
    })

    return { editor }
  },
}
</script>

<style lang="scss">
.tiptap-container {
  display: flex;
  width: 100%;
  justify-content: center;
  flex-direction: column;
  border: 2px solid var(--primary-alt);
  border-radius: 10px;

  .tiptap-buttons {
    display: flex;
    flex-direction: row;
    gap: 5px;
    padding: 10px;
    border-bottom: 1px solid var(--primary-alt);

    button{
      padding: 0;
      background: none;
      color: #000;
    }

    is-active{
      background: #cccbcb;
    }
  }

  .tiptap {
    padding: 5px 10px;
    width: 100%;
    display: flex;
    gap: 5px;
    flex-direction: column;
    overflow-y: auto;
    height: 200px;
    max-height: 500px;
    outline: none;

    :first-child {
      margin-top: 0;
    }

    /* List styles */
    ul,
    ol {
      padding: 0 1rem;
      margin: 1.25rem 1rem 1.25rem 0.4rem;

      li p {
        margin-top: 0.25em;
        margin-bottom: 0.25em;
      }
    }

    /* Heading styles */
    h1,
    h2,
    h3,
    h4,
    h5,
    h6 {
      line-height: 1.1;
      margin-top: 2.5rem;
      text-wrap: pretty;
    }

    h1,
    h2 {
      margin-top: 3.5rem;
      margin-bottom: 1.5rem;
    }

    h1 {
      font-size: 1.4rem;
    }

    h2 {
      font-size: 1.2rem;
    }

    h3 {
      font-size: 1.1rem;
    }

    h4,
    h5,
    h6 {
      font-size: 1rem;
    }

    table{
      width: 100%;
    }

    table, th, td{
      border: 1px solid #000;
      border-collapse: collapse;
    }

    /* Code and preformatted text styles */
    code {
      background-color: var(--purple-light);
      border-radius: 0.4rem;
      color: var(--black);
      font-size: 0.85rem;
      padding: 0.25em 0.3em;
    }

    pre {
      background: var(--black);
      border-radius: 0.5rem;
      color: var(--white);
      font-family: 'JetBrainsMono', monospace;
      margin: 1.5rem 0;
      padding: 0.75rem 1rem;

      code {
        background: none;
        color: inherit;
        font-size: 0.8rem;
        padding: 0;
      }
    }

    blockquote {
      border-left: 3px solid var(--gray-3);
      margin: 1.5rem 0;
      padding-left: 1rem;
    }

    hr {
      border: none;
      border-top: 1px solid var(--gray-2);
      margin: 2rem 0;
    }
  }
}
</style>