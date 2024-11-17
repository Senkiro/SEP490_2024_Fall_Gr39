<template>
  <div class="tiptap-container">
    <div v-if="editor" class="tiptap-buttons">
      <button @click="editor.chain().focus().toggleBold().run()"
              :disabled="!editor.can().chain().focus().toggleBold().run()" :class="{ 'is-active': editor.isActive('bold') }">
        <BoldIcon />
      </button>
      <button @click="editor.chain().focus().toggleItalic().run()"
              :disabled="!editor.can().chain().focus().toggleItalic().run()"
              :class="{ 'is-active': editor.isActive('italic') }">
        <ItalicIcon/>
      </button>
      <button @click="editor.chain().focus().toggleUnderline().run()"
              :disabled="!editor.can().chain().focus().toggleUnderline().run()"
              :class="{ 'is-active': editor.isActive('underline') }">
        <UnderlineIcon/>
      </button>
      <button @click="editor.chain().focus().toggleHeading({ level: 1 }).run()"
              :class="{ 'is-active': editor.isActive('heading', { level: 1 }) }">
        <Heading1Icon/>
      </button>
      <button @click="editor.chain().focus().toggleHeading({ level: 2 }).run()"
              :class="{ 'is-active': editor.isActive('heading', { level: 2 }) }">
        <Heading2Icon/>
      </button>
      <button @click="editor.chain().focus().toggleHeading({ level: 3 }).run()"
              :class="{ 'is-active': editor.isActive('heading', { level: 3 }) }">
        <Heading3Icon/>
      </button>
      <button @click="editor.chain().focus().toggleBulletList().run()"
              :class="{ 'is-active': editor.isActive('bulletList') }">
        <BulletListIcon/>
      </button>
      <button @click="editor.chain().focus().toggleOrderedList().run()"
              :class="{ 'is-active': editor.isActive('orderedList') }">
        <OrderedListIcon/>
      </button>
      <button @click="editor.chain().focus().insertTable({ rows: 3, cols: 3, withHeaderRow: true }).run()">
        <TableIcon/>
      </button>
      <button @click="editor.chain().focus().addColumnAfter().run()">
        <AddColumnIcon/>
      </button>
      <button @click="editor.chain().focus().deleteColumn().run()">
        <RemoveColumnIcon/>
      </button>
      <button @click="editor.chain().focus().addRowAfter().run()">
        <AddRowIcon/>
      </button>
      <button @click="editor.chain().focus().deleteRow().run()">
        <RemoveRowIcon/>
      </button>
      <button @click="editor.chain().focus().deleteTable().run()">
        <RemoveTableIcon/>
      </button>
      <button @click="editor.chain().focus().mergeCells().run()">
        <MergeCellIcon/>
      </button>
      <button @click="editor.chain().focus().splitCell().run()">
        <SplitCellIcon/>
      </button>
    </div>
    <editor-content :editor="editor"/>
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

import BoldIcon from 'vue-material-design-icons/FormatBold.vue';
import ItalicIcon from 'vue-material-design-icons/FormatItalic.vue';
import UnderlineIcon from 'vue-material-design-icons/FormatUnderline.vue';
import Heading1Icon from 'vue-material-design-icons/FormatHeader1.vue';
import Heading2Icon from 'vue-material-design-icons/FormatHeader2.vue';
import Heading3Icon from 'vue-material-design-icons/FormatHeader3.vue';
import BulletListIcon from 'vue-material-design-icons/FormatListBulleted.vue';
import OrderedListIcon from 'vue-material-design-icons/FormatListNumbered.vue';
import TableIcon from 'vue-material-design-icons/Table.vue';
import AddColumnIcon from 'vue-material-design-icons/TableColumnPlusAfter.vue';
import AddRowIcon from 'vue-material-design-icons/TableRowPlusAfter.vue';
import RemoveColumnIcon from 'vue-material-design-icons/TableColumnRemove.vue';
import RemoveRowIcon from 'vue-material-design-icons/TableRowRemove.vue';
import RemoveTableIcon from 'vue-material-design-icons/TableRemove.vue';
import MergeCellIcon from 'vue-material-design-icons/TableMergeCells.vue';
import SplitCellIcon from 'vue-material-design-icons/TableSplitCell.vue';


import {Editor, EditorContent} from '@tiptap/vue-3'

export default {
  components: {
    EditorContent,
    BoldIcon,
    ItalicIcon,
    UnderlineIcon,
    Heading1Icon,
    Heading2Icon,
    Heading3Icon,
    BulletListIcon,
    OrderedListIcon,
    TableIcon,
    AddColumnIcon,
    RemoveColumnIcon,
    AddRowIcon,
    RemoveRowIcon,
    RemoveTableIcon,
    MergeCellIcon,
    SplitCellIcon
  },

  props: {
    modelValue: {
      type: String,
      default: '',
    },
  },

  emits: ['update:modelValue'],

  watch: {
    modelValue(value) {
      // HTML
      const isSame = this.editor.getHTML() === value

      // JSON
      // const isSame = JSON.stringify(this.editor.getJSON()) === JSON.stringify(value)

      if (isSame) {
        return
      }

      this.editor.commands.setContent(value, false)
    },
  },
  data() {
    return {
      editor: null,
    }
  },

  mounted() {
    this.editor = new Editor({
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
      content: this.modelValue,
      onUpdate: () => {
        // HTML
        this.$emit('update:modelValue', this.editor.getHTML())

        // JSON
        // this.$emit('update:modelValue', this.editor.getJSON())
      },
    })
  },

  beforeUnmount() {
    this.editor.destroy()
  },
}
</script>

<style lang="scss">
.tiptap-container {
  display: flex;
  width: 100%;
  justify-content: center;
  flex-direction: column;
  border: 2px solid var(--primary);
  border-radius: 10px;

  .tiptap-buttons {
    display: flex;
    flex-direction: row;
    gap: 10px;
    padding: 10px;
    border-bottom: 1px solid var(--primary);

    button {
      padding: 0;
      background: none;
      color: var(--primary);
      padding: 3px;

      .material-design-icon {
        display: flex;
      }
    }

    .is-active {
      background: #c4cceb;

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

    h1 {
      font-size: 36px;
      background: -webkit-linear-gradient(180deg, #304CB2, #1A2C6F);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      font-weight: bold;
      margin: 20px 0px;
    }

    h2 {
      font-size: 1.2rem;
    }

    h3 {
      font-size: 1.1rem;
      font-weight: bold;
    }

    h4,
    h5,
    h6 {
      font-size: 1rem;
    }

    table,
    th,
    td {
      border: 1px solid #1A2C6F;
    }

    th,
    td,
    tr {
      display: flex;
      width: 100%;
    }

    table {
      border-collapse: collapse;
      border-radius: 20px;
      width: 100%;

      p {
        width: fit-content;
        display: flex;
      }

      .center {
        text-align: center;
      }

      th {
        text-align: left;
        padding: 10px 20px;
        background: #dae4f3;
        width: auto;
        display: flex;
        width: 100%;
      }

      tr {
        text-align: left;
        display: flex;
        width: 100%;

        td {
          padding: 5px 20px;

          .icon-group {
            display: flex;
            gap: 10px;
            justify-content: center;
          }
        }
      }
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